package com.DTISE.ShelfMasterBE.infrastructure.product.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.CreateProductRequest;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.UpdateProductRequest;
import com.DTISE.ShelfMasterBE.usecase.product.CreateProductUseCase;
import com.DTISE.ShelfMasterBE.usecase.product.DeleteProductUseCase;
import com.DTISE.ShelfMasterBE.usecase.product.GetProductsUseCase;
import com.DTISE.ShelfMasterBE.usecase.product.UpdateProductUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetProductsUseCase getProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductsUseCase getProductsUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.getProductsUseCase = getProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Validated CreateProductRequest req) {
        return ApiResponse.successfulResponse(
                "Create new product success",
                createProductUseCase.createProduct(req));
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer length,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<Long> category,
            @RequestParam(required = false) Long warehouseId) {
        return ApiResponse.successfulResponse(
                "Products retrieved successfully",
                Pagination.mapResponse(getProductsUseCase
                        .getProducts(
                                Pagination.createPageable(start, length, field, order),
                                search,
                                category,
                                warehouseId))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetail(
            @PathVariable Long id
    ) {
        return ApiResponse.successfulResponse(
                "Product retrieved successfully",
                getProductsUseCase.getProductDetail(id));
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody @Validated UpdateProductRequest req) {
        return ApiResponse.successfulResponse(
                "Product updated successfully",
                updateProductUseCase.updateProduct(id, req));
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        deleteProductUseCase.deleteProduct(id);
        return ApiResponse.successfulResponse("Product deleted successfully");
    }
}
