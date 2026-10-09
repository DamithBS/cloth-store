package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.BrandRequest;
import com.example.clothes_store.Controller.DTO.Response.BrandResponse;
import com.example.clothes_store.Exception.BrandAlreadyExistsException;
import com.example.clothes_store.Exception.BrandNotFoundException;
import com.example.clothes_store.Model.Entity.Brand;
import com.example.clothes_store.Repository.BrandRepository;
import com.example.clothes_store.Service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    //Creates a new brand after checking for duplicate brand names.
    @Override
    @CacheEvict(value = "brands", allEntries = true)
    public void create(BrandRequest brandRequest){

        String BrandName = brandRequest.getName().trim();

        if(brandRepository.existsByNameIgnoreCase(BrandName))
        {
            throw new BrandAlreadyExistsException("Brand with this name already exists");
        }

        Brand brand = new Brand();
        brand.setName(BrandName);
        brandRepository.save(brand);
    }


    //Returns all brands from the database.
    @Override
    @Cacheable(value = "brands", key = "'all'")

    public List<BrandResponse> findAll(){

        log.info("========== DATABASE HIT ==========");

        return brandRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


//    Finds a brand by its ID.
//    Throws an exception if the brand is not found
    @Override
    @Cacheable(value = "brands" , key = "#id")
    public BrandResponse findById(Long id){
        Brand brand = brandRepository.findById(id).orElseThrow(
                ()-> new BrandNotFoundException("Brand not found..")
        );


        return mapToResponse(brand);
    }

    //Updates an existing brand with new name
    @Override
    @CacheEvict(value = "brands", allEntries = true)
    public void update(Long id ,BrandRequest brandRequest){
        Brand brand = brandRepository.findById(id).orElseThrow(
                ()-> new BrandNotFoundException("Brand not found...")
        );

        String brandName = brandRequest.getName().trim();

        if (!brand.getName().equalsIgnoreCase(brandName)
                && brandRepository.existsByNameIgnoreCase(brandName)){
            throw new BrandNotFoundException("Brand name not found");
        }

        brand.setName(brandName);
        brandRepository.save(brand);
    }

    //Deletes a brand by ID after confirming it exists.
    @Override
    @CacheEvict(value = "brands", allEntries = true)
    public void delete(Long id){
        Brand brand = brandRepository.findById(id).orElseThrow(
                ()-> new BrandNotFoundException("Brand not found...")
        );
        brandRepository.delete(brand);
    }


    // ENTITY -> DTO MAPPING
    private BrandResponse mapToResponse(Brand brand){

        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setId(brand.getId());
        brandResponse.setName(brand.getName());

        return brandResponse;
    }
}
