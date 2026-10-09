package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.SubCategoryRequest;
import com.example.clothes_store.Controller.DTO.Response.SubCategoryResponse;
import com.example.clothes_store.Model.Entity.SubCategory;
import com.example.clothes_store.Service.SubCategoryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sub-category")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    //Create a new subcategory under an existing category.
    @RolesAllowed("ADMIN")
    @PostMapping("/{categoryId}")
    public ResponseEntity<String> createSubCategory(
            @PathVariable("categoryId") Long categoryId ,
            @Valid @RequestBody SubCategoryRequest subCategoryRequest
    ){
        subCategoryService.create(categoryId,subCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Sub Category Created Successfully..");
    }


    //Fetch all subcategories and convert them into response DTOs.
    @GetMapping
    public ResponseEntity<List<SubCategoryResponse>> allSubCategory(){

        return ResponseEntity.ok(subCategoryService.findAll());
    }


    //Fetch a single subcategory by ID.
    @GetMapping("/{subCategoryId}")
    public ResponseEntity<SubCategoryResponse> getSubCategoryByID(
            @PathVariable("subCategoryId") Long subCategoryId
    ){
       return ResponseEntity.ok(subCategoryService.findById(subCategoryId));
    }


    //Update an existing subcategory by ID.
    @RolesAllowed("ADMIN")
    @PutMapping("/{subCategoryId}")
    public ResponseEntity<String> updateSubCategory(
            @PathVariable("subCategoryId") Long subCategoryId,
            @Valid @RequestBody SubCategoryRequest subCategoryRequest
    ){
        subCategoryService.update(subCategoryId,subCategoryRequest);
        return ResponseEntity.ok("sub category updated Successfully..");
    }

    //Delete a subcategory by ID.
    @RolesAllowed("ADMIN")
    @DeleteMapping("/{subCategoryId}")
    public ResponseEntity<String> deleteSubCategory(
            @PathVariable("subCategoryId") Long subCategoryId
    ){
        subCategoryService.delete(subCategoryId);
        return ResponseEntity.ok("sub category deleted Successfully..");
    }



}

