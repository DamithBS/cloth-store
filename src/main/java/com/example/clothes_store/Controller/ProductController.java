package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.ProductRequest;
import com.example.clothes_store.Controller.DTO.Response.ProductResponse;
import com.example.clothes_store.Controller.DTO.Response.ProductVariantResponse;
import com.example.clothes_store.Model.Entity.Product;
import com.example.clothes_store.Model.Entity.ProductImage;
import com.example.clothes_store.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(
            @Valid @RequestBody ProductRequest productRequest
    ){
        productService.create(productRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Product created successfully");
    }


    @GetMapping
    public List<ProductResponse> getAllProduct(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ){
        List<Product> productList = productService.findAll(PageRequest.of(page,size));
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product : productList){
            ProductResponse productResponse =new ProductResponse();

            productResponse.setName(product.getName());
            productResponse.setDescription(product.getDescription());
            productResponse.setBasePrice(product.getBasePrice());
            productResponse.setCategoryName(product.getSubCategory().getCategory().getName());
            productResponse.setBrandName(product.getBrand().getName());
            productResponse.setSubCategoryName(product.getSubCategory().getName());


            List<ProductVariantResponse> variantResponses = new ArrayList<>();

            if(product.getProductVariants() != null){
                product.getProductVariants().forEach(variant ->{

                    ProductVariantResponse productVariantResponse = new ProductVariantResponse();

                    productVariantResponse.setColor(variant.getColor());
                    productVariantResponse.setSize(variant.getSize());
                    productVariantResponse.setSku(variant.getSku());
                    productVariantResponse.setPrice(variant.getPrice());
                    productVariantResponse.setStockQuantity(variant.getInventory().getStockQuantity());

                    variantResponses.add(productVariantResponse);

                });
            }

            productResponse.setVariants(variantResponses);
            productResponseList.add(productResponse);

            //Images
            List<String> imageUrls = new ArrayList<>();

            if(product.getProductImages() != null){
                product.getProductImages().forEach(img -> {
                    imageUrls.add(img.getImageUrl());
                });
            }

            productResponse.setImages(imageUrls);
        }
        return productResponseList;
    }


    @GetMapping("/{productId}")
    public ProductResponse productFindById(
            @PathVariable("productId") Long id
    ){
        Product product = productService.findById(id);
        ProductResponse productResponse = new ProductResponse();

        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setBasePrice(product.getBasePrice());
        productResponse.setCategoryName(product.getSubCategory().getCategory().getName());
        productResponse.setBrandName(product.getBrand().getName());
        productResponse.setSubCategoryName(product.getSubCategory().getName());


        List<ProductVariantResponse> variantResponses = new ArrayList<>();

        if(product.getProductVariants() != null){
            product.getProductVariants().forEach(variant ->{

                ProductVariantResponse productVariantResponse = new ProductVariantResponse();

                productVariantResponse.setColor(variant.getColor());
                productVariantResponse.setSize(variant.getSize());
                productVariantResponse.setSku(variant.getSku());
                productVariantResponse.setPrice(variant.getPrice());
                productVariantResponse.setStockQuantity(variant.getInventory().getStockQuantity());

                variantResponses.add(productVariantResponse);

            });
        }
        productResponse.setVariants(variantResponses);


        //Images
        List<String> imageUrls = new ArrayList<>();

        if(product.getProductImages() != null){
            product.getProductImages().forEach(img -> {
                imageUrls.add(img.getImageUrl());
            });
        }

        productResponse.setImages(imageUrls);

        return productResponse;

    }


    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable("productId") Long id,
            @Valid @RequestBody ProductRequest productRequest
    ){
        productService.update(id, productRequest);
        return ResponseEntity.ok("Product updated successfully");
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("productId") Long id
    ){
        productService.delete(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
