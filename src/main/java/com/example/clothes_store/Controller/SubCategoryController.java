package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.SubCategoryRequest;
import com.example.clothes_store.Controller.DTO.Response.SubCategoryResponse;
import com.example.clothes_store.Model.Entity.SubCategory;
import com.example.clothes_store.Service.SubCategoryService;
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
    public List<SubCategoryResponse> allSubCategory(){

        //Get all subcategories from service layer
        List<SubCategory> subCategoryList = subCategoryService.findAll();

        // Convert entity list into response DTO list
        List<SubCategoryResponse> responseList = new ArrayList<>();

        for (SubCategory subCategory : subCategoryList){
            responseList.add(mapToResponse(subCategory));
        }
        return responseList;
    }


    //Fetch a single subcategory by ID.
    @GetMapping("/{subCategoryId}")
    public SubCategoryResponse getSubCategoryByID(
            @PathVariable("subCategoryId") Long subCategoryId
    ){
        SubCategory subCategory = subCategoryService.findById(subCategoryId);

        return mapToResponse(subCategory);
    }

    //Update an existing subcategory by ID.
    @PutMapping("/{subCategoryId}")
    public ResponseEntity<String> updateSubCategory(
            @PathVariable("subCategoryId") Long subCategoryId,
            @Valid @RequestBody SubCategoryRequest subCategoryRequest
    ){
        subCategoryService.update(subCategoryId,subCategoryRequest);
        return ResponseEntity.ok("sub category updated Successfully..");
    }

    //Delete a subcategory by ID.
    @DeleteMapping("/{subCategoryId}")
    public ResponseEntity<String> deleteSubCategory(
            @PathVariable("subCategoryId") Long subCategoryId
    ){
        subCategoryService.delete(subCategoryId);
        return ResponseEntity.ok("sub category deleted Successfully..");
    }




    //Helper method to convert SubCategory entity into SubCategoryResponse DTO.
    private SubCategoryResponse mapToResponse(SubCategory subCategory) {
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        subCategoryResponse.setName(subCategory.getName());
        subCategoryResponse.setCategoryName(subCategory.getCategory().getName());
        subCategoryResponse.setCategoryDescriptions(subCategory.getCategory().getDescription());
        return subCategoryResponse;
    }
}

