package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.BrandRequest;
import com.example.clothes_store.Exception.BrandAlreadyExistsException;
import com.example.clothes_store.Exception.BrandNotFoundException;
import com.example.clothes_store.Model.Entity.Brand;
import com.example.clothes_store.Repository.BrandRepository;
import com.example.clothes_store.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    //Creates a new brand after checking for duplicate brand names.
    @Override
    public void create(BrandRequest brandRequest){

        String BrandName = brandRequest.getName().trim();

        if(brandRepository.existsByNameIgnoreCase(BrandName)){
            throw new BrandAlreadyExistsException("Brand with this name already exists");
        }

        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brandRepository.save(brand);
    }


    //Returns all brands from the database.
    @Override
    public List<Brand> findAll(){
        return brandRepository.findAll();
    }


//    Finds a brand by its ID.
//    Throws an exception if the brand is not found
    @Override
    public Brand findById(Long id){
        return brandRepository.findById(id).orElseThrow(
                ()-> new BrandNotFoundException("Brand not found..")
        );
    }

    //Updates an existing brand with new name
    @Override
    public void update(Long id ,BrandRequest brandRequest){
        Brand brand = brandRepository.findById(id).orElseThrow(
                ()-> new BrandNotFoundException("Brand not found...")
        );

        brand.setName(brandRequest.getName());
        brandRepository.save(brand);
    }

    //Deletes a brand by ID after confirming it exists.
    @Override
    public void delete(Long id){
        Brand brand = brandRepository.findById(id).orElseThrow(
                ()-> new BrandNotFoundException("Brand not found...")
        );
        brandRepository.delete(brand);
    }

}
