package com.future.booklook.controller;

import com.future.booklook.model.entity.Category;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.response.ApiResponse;
import com.future.booklook.payload.request.EditProductRequest;
import com.future.booklook.payload.response.ProductInfoResponse;
import com.future.booklook.service.impl.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Api
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private MarketServiceImpl marketService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(
            @RequestParam("title") @NotBlank String title,
            @RequestParam("pageTotal") @NotBlank Long pageTotal,
            @RequestParam("description") @NotBlank String description,
            @RequestParam("author") @NotBlank String author,
            @RequestParam("publisher") @NotBlank String publisher,
            @RequestParam("isbn") @NotBlank String isbn,
            @RequestParam("price") @NotBlank String price,
            @RequestParam("picture") @NotBlank MultipartFile picture,
            @RequestParam("book") @NotBlank MultipartFile book,
            @RequestParam("categories") @NotBlank String categories
    ){
        User user = userService.getUserFromSession();
        Market market = marketService.findByUser(user);

        String[] categoryArray = categories.split(", ");
        Set<Category> categoriesSet = new HashSet<>();
        for (String category:categoryArray) {
            categoriesSet.add(this.categoryService.findByCategoryName(category));
        }

        String fileName = "";

        fileName = fileStorageService.storeFile(book, "books");
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/books/")
                .path(fileName)
                .toUriString();

        fileName = fileStorageService.storeFile(picture, "products");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/products/")
                .path(fileName)
                .toUriString();

        StringBuilder newSku = new StringBuilder();
        Long totalProduct = market.getTotalProduct() + 1;
        newSku.append(market.getMarketCode() + "-");
        for(int i = 0; i < (4 - totalProduct.toString().length()); i++){
            newSku.append("0");
        }
        newSku.append(totalProduct.toString());
        String finalNewSku = newSku.toString();

        market.setTotalProduct(totalProduct);
        Product product = new Product(
                title,
                pageTotal,
                author,
                publisher,
                isbn,
                finalNewSku,
                description,
                Long.parseLong(price),
                categoriesSet,
                market,
                photoUri,
                bookUri
        );
        user.setReadKey(generateRandomString());

        userService.save(user);
        marketService.save(market);
        productService.save(product);
        return new ResponseEntity<>(new ApiResponse(true, "Product created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getProductsFromCategory(@PathVariable String categoryName){
        if(!(categoryService.categoryExistByCategoryName(categoryName))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Category category = categoryService.findByCategoryName(categoryName);
        Set<Category> categoriesSet = new HashSet<>();
        categoriesSet.add(category);

        return new ResponseEntity<>(productService.findProductsByCategories(categoriesSet), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductFromProductId(@PathVariable String productId){
        if(!(productService.existsByProductId(productId))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Product product = productService.findByProductId(productId);
        Market market = marketService.findMarketByProduct(product);
        String marketId = market.getMarketId();
        String marketName = market.getMarketName();

        return new ResponseEntity<>(new ProductInfoResponse(product, marketId, marketName), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editProduct(@RequestBody EditProductRequest editProductRequest){
        if(!(productService.existsByProductId(editProductRequest.getProductId()))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Product product = productService.findByProductId(editProductRequest.getProductId());

        product.setPrice(Long.parseLong(editProductRequest.getPrice()));
        product.setDescription(editProductRequest.getDescription());

        productService.save(product);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been Edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/photo")
    public ResponseEntity<?> editPhotoProduct(@RequestParam String productId, @RequestParam MultipartFile picture){
        if(!(productService.existsByProductId(productId))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Product product = productService.findByProductId(productId);

        String fileName = fileStorageService.storeFile(picture, "products");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/products/")
                .path(fileName)
                .toUriString();

        product.setProductPhoto(photoUri);
        productService.save(product);
        return new ResponseEntity<>(new ApiResponse(true, "Product Photo has been Edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/book")
    public ResponseEntity<?> editFileProduct(@RequestParam String productId, @RequestParam MultipartFile book){
        if(!(productService.existsByProductId(productId))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Product product = productService.findByProductId(productId);

        String fileName = fileStorageService.storeFile(book, "books");
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/books/")
                .path(fileName)
                .toUriString();

        product.setProductPhoto(bookUri);
        productService.save(product);
        return new ResponseEntity<>(new ApiResponse(true, "Book file has been Edited successfully"), HttpStatus.OK);
    }

    @GetMapping("/market/{marketId}")
    public ResponseEntity<?> allProductFromMarket(@PathVariable String marketId){
        if(!(marketService.marketExistByMarketId(marketId))){
            return new ResponseEntity<>(new ApiResponse(false, "Market does not exist"), HttpStatus.BAD_REQUEST);
        }

        Market market = marketService.findByMarketId(marketId);
        Set<Product> products = productService.findAllByMarketAndConfirmed(market);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/market/auth/all")
    public ResponseEntity<?> allProductFromAuthenticatedMarket(){
        User user = userService.getUserFromSession();
        if(!(marketService.marketExistByUser(user))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Market market = user.getMarket();
        Set<Product> products = productService.findAllByMarket(market);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/dashboard/header/{numberOfLimit}")
    public ResponseEntity<?> getAllProductLimited(@PathVariable Integer numberOfLimit){
        return new ResponseEntity<>(productService.getAllConfirmedProductLimited(numberOfLimit).getContent(), HttpStatus.OK);
    }

    private String generateRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 16;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
