package com.future.booklook.controller;

import com.future.booklook.model.entity.Category;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.security.UserPrincipal;
import com.future.booklook.service.impl.*;
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

        MultipartFile pictureFile = picture;
        String fileName = fileStorageService.storeFile(pictureFile, "products");
        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/products/")
                .path(fileName)
                .toUriString();

        Product product = new Product(
                title,
                author,
                publisher,
                market.getMarketSKU()+"-"+sku,
                description,
                Long.parseLong(price),
                categoriesSet,
                market,
                fileUri
        );

        productService.save(product);
        return new ResponseEntity(new ApiResponse(true, "Product created successfully"), HttpStatus.OK);
    }

    @GetMapping("/{categoryName}")
    public Set<Product> getProductsFromCategory(@PathVariable String categoryName){
        Category category = categoryService.findByCategoryName(categoryName);
        Set<Category> categoriesSet = new HashSet<>();
        categoriesSet.add(category);

        Set<Product> products = productService.findProductsByCategories(categoriesSet);

        return products;
    }

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }
}
