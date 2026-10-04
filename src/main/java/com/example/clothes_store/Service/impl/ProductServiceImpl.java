package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.ProductRequest;
import com.example.clothes_store.Controller.DTO.Request.ProductVariantRequest;
import com.example.clothes_store.Exception.BrandNotFoundException;
import com.example.clothes_store.Exception.ProductNotFoundException;
import com.example.clothes_store.Exception.SubCategoryNotFoundException;
import com.example.clothes_store.Model.Entity.*;
import com.example.clothes_store.Model.Eum.ProductStatus;
import com.example.clothes_store.Repository.*;
import com.example.clothes_store.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private UserRepository userRepository;


    // CREATE PRODUCT
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductRequest productRequest, String userName){

        // Find authenticated admin
        User admin = userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("Admin user not found")
                );

        // find Product name
        String productName = productRequest.getName().trim();

        if (productRepository.existsByNameIgnoreCase(productName)){
            throw new RuntimeException("Product already exists");
        }

        // Validate SKU
        validateSkus(productRequest);

        // Create Product
        Product product =new Product();
        product.setName(productName);
        product.setDescription(productRequest.getDescription());
        product.setBasePrice(productRequest.getBasePrice());
        product.setCreationAt(LocalDateTime.now());
        product.setProductStatus(ProductStatus.ACTIVE);

        // Save authenticated admin
        product.setUser(admin);


        // Sub Category
        product.setSubCategory(
                subCategoryRepository.findById(productRequest.getSubCategoryId())
                        .orElseThrow(()-> new SubCategoryNotFoundException("sub category not found"))
        );

        // Brand
        product.setBrand(
                brandRepository.findById(productRequest.getBrandId())
                        .orElseThrow(()->new BrandNotFoundException("brand not found"))
        );

        // Images
        if(productRequest.getImages() != null){
            for(String url : productRequest.getImages()){
                ProductImage image = new ProductImage();
                image.setImageUrl(url);
                image.setProduct(product);
                product.getProductImages().add(image);
            }
        }


        // Variants + Inventory
        if(productRequest.getVariants() != null){
            for (ProductVariantRequest variantRequest : productRequest.getVariants()){
                ProductVariant variant =new ProductVariant();
                variant.setSize(variantRequest.getSize());
                variant.setColor(variantRequest.getColor());
                variant.setSku(variantRequest.getSku().trim());
                variant.setPrice(variantRequest.getPrice());

                variant.setProduct(product);
                product.getProductVariants().add(variant);


                // Inventory
                Inventory inventory = new Inventory();
                inventory.setStockQuantity(
                        variantRequest.getStockQuantity() != null ? variantRequest.getStockQuantity() : 0);

                inventory.setProductVariant(variant);
                variant.setInventory(inventory);
            }
        }

        //save product
        productRepository.save(product);
    }

    // GET ALL ACTIVE PRODUCTS
    @Override
    public List<Product> findAll(Pageable pageable){
        Page<Product> productPage =  productRepository.findAll(pageable);
        return productPage.getContent();
    }

    // GET PRODUCT BY ID
    @Override
    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("product not found..")
        );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ProductRequest productRequest){

        // find product
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("product not found..")
        );

        // find Product name already exists
        String productName = productRequest.getName().trim();

        if (!product.getName().equalsIgnoreCase(productName)){
            if (productRepository.existsByNameIgnoreCase(productName)){
                throw new IllegalStateException( "Product with this name already exists");

            }
        }

        // Update basic details
        product.setName(productName);
        product.setDescription(productRequest.getDescription());
        product.setBasePrice(productRequest.getBasePrice());


        // Sub Category
        product.setSubCategory(
                subCategoryRepository.findById(productRequest.getSubCategoryId())
                        .orElseThrow(()-> new SubCategoryNotFoundException("sub category not found"))
        );

        // Brand
        product.setBrand(
                brandRepository.findById(productRequest.getBrandId())
                        .orElseThrow(()->new BrandNotFoundException("brand not found"))
        );

        // images
        product.getProductImages().clear();

        productRequest.getImages().forEach(url -> {
            ProductImage image = new ProductImage();
            image.setImageUrl(url);
            image.setProduct(product);
            product.getProductImages().add(image);
        });


        // Variants + Inventory
        if (productRequest.getVariants() != null) {
            for (ProductVariantRequest productVariantRequest : productRequest.getVariants()) {

                // Existing variant
                if (productVariantRequest.getId() != null) {
                    ProductVariant productVariant = product.getProductVariants()
                            .stream()
                            .filter(v -> v.getId().equals(productVariantRequest.getId()))
                            .findFirst()
                            .orElseThrow(
                                    () -> new RuntimeException("Variant does not belong to this produc")
                            );

                    productVariant.setSize(productVariantRequest.getSize().trim());
                    productVariant.setColor(productVariantRequest.getColor().trim());
                    productVariant.setPrice(productVariantRequest.getPrice());

                    // Inventory
                    if (productVariant.getInventory() == null) {
                        Inventory inventory = new Inventory();

                        inventory.setProductVariant(productVariant);
                        productVariant.setInventory(inventory);
                    }

                    productVariant.getInventory().setStockQuantity(
                            productVariantRequest.getStockQuantity()!= null ? productVariantRequest.getStockQuantity() : 0
                    );
                }
            }

        }

        productRepository.save(product);
    }



    // DELETE PRODUCT - SOFT DELETE
    @Override
    @Transactional
    public void delete(Long id){
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("product not found")
        );
        productRepository.delete(product);
    }


    //--------------------------------------
    // HELPER METHODS


    // CREATE SKU VALIDATION
    private void validateSkus(ProductRequest productRequest){

        if (productRequest.getVariants() == null){
            return;
        }

        Set<String> skuSet = new HashSet<>();

        for (ProductVariantRequest productVariantRequest : productRequest.getVariants()){

            if (productVariantRequest.getSku() == null || productVariantRequest.getSku().isBlank()){
                throw  new RuntimeException( "SKU is required when creating a variant");
            }

            String sku = productVariantRequest.getSku().trim().toLowerCase();

            // Duplicate inside current request
            if (!skuSet.add(sku)){
                throw new IllegalStateException("Duplicate SKU in request: " + sku);

            }

            // Duplicate in database
            if (productVariantRepository.existsBySkuIgnoreCase(sku)){
                throw new IllegalStateException("SKU already exists: " + sku);
            }



        }
    }



}
