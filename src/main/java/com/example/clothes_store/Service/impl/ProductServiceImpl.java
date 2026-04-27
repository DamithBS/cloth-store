package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.ProductRequest;
import com.example.clothes_store.Controller.DTO.Request.ProductVariantRequest;
import com.example.clothes_store.Exception.BrandNotFoundException;
import com.example.clothes_store.Exception.ProductNotFoundException;
import com.example.clothes_store.Exception.SubCategoryNotFoundException;
import com.example.clothes_store.Model.Entity.Inventory;
import com.example.clothes_store.Model.Entity.Product;
import com.example.clothes_store.Model.Entity.ProductImage;
import com.example.clothes_store.Model.Entity.ProductVariant;
import com.example.clothes_store.Model.Eum.ProductStatus;
import com.example.clothes_store.Repository.BrandRepository;
import com.example.clothes_store.Repository.ProductRepository;
import com.example.clothes_store.Repository.ProductVariantRepository;
import com.example.clothes_store.Repository.SubCategoryRepository;
import com.example.clothes_store.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductRequest productRequest){

        String productName = productRequest.getName().trim();

        if (productRepository.existsByNameIgnoreCase(productName)){
            throw new RuntimeException("Product already exists");
        }


        Set<String> skuSet = new HashSet<>();

        if(productRequest.getVariants() != null){
            for (ProductVariantRequest productVariantRequest : productRequest.getVariants()){

                String sku = productVariantRequest.getSku().trim();

                // check duplicate in DB
                if(productVariantRepository.existsBySkuIgnoreCase(sku)){
                    throw new RuntimeException("SKU already exists: " + sku);
                }
            }
        }


        Product product =new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setBasePrice(productRequest.getBasePrice());
        product.setCreationAt(LocalDateTime.now());
        product.setProductStatus(ProductStatus.ACTIVE);

        product.setSubCategory(
                subCategoryRepository.findById(productRequest.getSubCategoryId())
                        .orElseThrow(()-> new SubCategoryNotFoundException("sub category not found"))
        );

        product.setBrand(
                brandRepository.findById(productRequest.getBrandId())
                        .orElseThrow(()->new BrandNotFoundException("brand not found"))
        );

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
                variant.setSku(variantRequest.getSku());
                variant.setPrice(variantRequest.getPrice());

                variant.setProduct(product);
                product.getProductVariants().add(variant);

                Inventory inventory = new Inventory();
                inventory.setStockQuantity(
                        variantRequest.getStockQuantity() != null ? variantRequest.getStockQuantity() : 0);

                inventory.setProductVariant(variant);
                variant.setInventory(inventory);
            }
        }
        productRepository.save(product);
    }


    @Override
    public List<Product> findAll(Pageable pageable){
        Page<Product> productPage =  productRepository.findAll(pageable);
        return productPage.getContent();
    }

    @Override
    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("product not found..")
        );
    }


    @Override
    public void update(Long id, ProductRequest productRequest){
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("product not found..")
        );

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setBasePrice(productRequest.getBasePrice());
        product.setCreationAt(LocalDateTime.now());
        product.setProductStatus(ProductStatus.ACTIVE);

        product.setSubCategory(
                subCategoryRepository.findById(productRequest.getSubCategoryId())
                        .orElseThrow(()-> new SubCategoryNotFoundException("sub category not found"))
        );

        product.setBrand(
                brandRepository.findById(productRequest.getBrandId())
                        .orElseThrow(()->new BrandNotFoundException("brand not found"))
        );

        ///images
        product.getProductImages().clear();

        productRequest.getImages().forEach(url -> {
            ProductImage image = new ProductImage();
            image.setImageUrl(url);
            image.setProduct(product);
            product.getProductImages().add(image);
        });


        // Variants + Inventory
        product.getProductVariants().clear();

        productRequest.getVariants().forEach(v -> {

            ProductVariant variant = new ProductVariant();
            variant.setSize(v.getSize());
            variant.setColor(v.getColor());
            variant.setSku(v.getSku());
            variant.setPrice(v.getPrice());
            variant.setProduct(product);

            Inventory inventory = new Inventory();
            inventory.setStockQuantity(v.getStockQuantity());
            inventory.setProductVariant(variant);

            variant.setInventory(inventory);

            product.getProductVariants().add(variant);
        });


        productRepository.save(product);
    }



    public void delete(Long id){
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("product not found")
        );
        productRepository.delete(product);
    }



}
