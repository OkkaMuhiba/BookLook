package com.future.booklook.controller;

import com.future.booklook.model.entity.Category;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.model.entity.properties.ProductConfirm;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.EditProduct;
import com.future.booklook.payload.ProductInfoResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashSet;
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
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("isbn") String isbn,
            @RequestParam("sku") String sku,
            @RequestParam("price") String price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("book") MultipartFile book,
            @RequestParam("categories") String categories
    ){
        String userId = getUserPrincipal().getUserId();
        User user = userService.findByUserId(userId);
        Market market = marketService.findByUser(user);

        String[] categoryArray = categories.split(", ");
        Set<Category> categoriesSet = new HashSet<>();
        for (String category:categoryArray) {
            categoriesSet.add(this.categoryService.findByCategoryName(category));
        }

        String fileName = "";

        MultipartFile bookFile = book;
        fileName = fileStorageService.storeFile(bookFile, "books");
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/books/")
                .path(fileName)
                .toUriString();

        MultipartFile pictureFile = picture;
        fileName = fileStorageService.storeFile(pictureFile, "products");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/products/")
                .path(fileName)
                .toUriString();

        Product product = new Product(
                title,
                author,
                publisher,
                isbn,
                market.getMarketSKU()+"-"+sku,
                description,
                Long.parseLong(price),
                categoriesSet,
                market,
                photoUri,
                bookUri
        );

        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Product created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/category/{categoryName}")
    public Set<Product> getProductsFromCategory(@PathVariable String categoryName){
        Category category = categoryService.findByCategoryName(categoryName);
        Set<Category> categoriesSet = new HashSet<>();
        categoriesSet.add(category);

        Set<Product> products = productService.findProductsByCategories(categoriesSet);

        return products;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductFromSKU(@PathVariable String productId){
        Product product = productService.findByProductId(productId);
        Market market = marketService.findMarketByProduct(product);
        String marketId = market.getMarketId();
        String marketName = market.getMarketName();

        return new ResponseEntity(new ProductInfoResponse(product, marketId, marketName), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editProduct(@RequestBody EditProduct editProduct){
        Product product = productService.findByProductId(editProduct.getProductId());

        product.setPrice(Long.parseLong(editProduct.getPrice()));
        product.setDescription(editProduct.getDescription());

        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Product has been Edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/photo")
    public ResponseEntity<?> editPhotoProduct(@RequestParam String productId, @RequestParam MultipartFile picture){
        Product product = productService.findByProductId(productId);

        MultipartFile pictureFile = picture;
        String fileName = fileStorageService.storeFile(pictureFile, "products");
        String photoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/products/")
                .path(fileName)
                .toUriString();

        product.setProductPhoto(photoUri);
        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Product Photo has been Edited successfully"), HttpStatus.OK);
    }

    @PutMapping("/edit/book")
    public ResponseEntity<?> editFileProduct(@RequestParam String productId, @RequestParam MultipartFile book){
        Product product = productService.findByProductId(productId);

        MultipartFile pictureFile = book;
        String fileName = fileStorageService.storeFile(pictureFile, "books");
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/books/")
                .path(fileName)
                .toUriString();

        product.setProductPhoto(bookUri);
        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Book file has been Edited successfully"), HttpStatus.OK);
    }

    @GetMapping("/market/{marketId}")
    public ResponseEntity<?> allProductFromMarket(@PathVariable String marketId){
        if(!(marketService.marketExistByMarketId(marketId))){
            return new ResponseEntity(new ApiResponse(false, "Market does not exist"), HttpStatus.BAD_REQUEST);
        }

        Market market = marketService.findByMarketId(marketId);
        Set<Product> products = productService.findAllByMarketAndConfirmed(market);

        return new ResponseEntity(products, HttpStatus.OK);
    }

    @GetMapping("/market/auth/all")
    public ResponseEntity<?> allProductFromAuthenticatedMarket(){
        User user = userService.findByUserId(getUserPrincipal().getUserId());
        Market market = user.getMarket();
        Set<Product> products = productService.findAllByMarket(market);

        return new ResponseEntity(products, HttpStatus.OK);
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
