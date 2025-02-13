package com.DTISE.ShelfMasterBE.infrastructure.category.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.CreateCategoryRequest;
import com.DTISE.ShelfMasterBE.infrastructure.category.dto.UpdateCategoryRequest;
import com.DTISE.ShelfMasterBE.usecase.category.CreateCategoryUseCase;
import com.DTISE.ShelfMasterBE.usecase.category.DeleteCategoryUseCase;
import com.DTISE.ShelfMasterBE.usecase.category.GetCategoriesUseCase;
import com.DTISE.ShelfMasterBE.usecase.category.UpdateCategoryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
            CreateCategoryUseCase createCategoryUseCase, 
            GetCategoriesUseCase getCategoriesUseCase, 
            UpdateCategoryUseCase updateCategoryUseCase, 
            DeleteCategoryUseCase deleteCategoryUseCase
    ) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest req) {
        return ApiResponse.successfulResponse(
                "Create new category success",
                createCategoryUseCase.createCategory(req));
    }

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(defaultValue = "0") Integer start,
                                           @RequestParam(defaultValue = "10") Integer length,
                                           @RequestParam(defaultValue = "") String search) {
        return ApiResponse.successfulResponse(
                "Categories retrieved successfully",
                Pagination.mapResponse(
                        getCategoriesUseCase.getCategories(Pagination.createPageable(start, length),
                                search)
                )
        );
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                           @RequestBody UpdateCategoryRequest req) {
        return ApiResponse.successfulResponse(
                "Category updated successfully",
                updateCategoryUseCase.updateCategory(id, req));
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        deleteCategoryUseCase.deleteCategory(id);
        return ApiResponse.successfulResponse("Category deleted successfully");
    }
}
