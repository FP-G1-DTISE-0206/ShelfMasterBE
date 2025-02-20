package com.DTISE.ShelfMasterBE.infrastructure.productMutation.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AddProductStockRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.InternalProductMutationRequest;
import com.DTISE.ShelfMasterBE.usecase.productMutation.AddProductStockUseCase;
import com.DTISE.ShelfMasterBE.usecase.productMutation.CreateProductMutationUseCase;
import com.DTISE.ShelfMasterBE.usecase.productMutation.GetProductMutationUseCase;
import com.DTISE.ShelfMasterBE.usecase.productMutation.RejectOrCancelProductMutationUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-mutation")
@PreAuthorize("hasAnyAuthority('SCOPE_SUPER_ADMIN', 'SCOPE_WH_ADMIN')")
public class ProductMutationController {
    private final AddProductStockUseCase addProductStockUseCase;
    private final GetProductMutationUseCase getProductMutationUseCase;
    private final CreateProductMutationUseCase createProductMutationUseCase;
    private final RejectOrCancelProductMutationUseCase rejectOrCancelProductMutationUseCase;

    public ProductMutationController(
            AddProductStockUseCase addProductStockUseCase,
            GetProductMutationUseCase getProductMutationUseCase,
            CreateProductMutationUseCase createProductMutationUseCase,
            RejectOrCancelProductMutationUseCase rejectOrCancelProductMutationUseCase
    ) {
        this.addProductStockUseCase = addProductStockUseCase;
        this.getProductMutationUseCase = getProductMutationUseCase;
        this.createProductMutationUseCase = createProductMutationUseCase;
        this.rejectOrCancelProductMutationUseCase = rejectOrCancelProductMutationUseCase;
    }

    @PostMapping("/add-stock")
    public ResponseEntity<?> addStock(@RequestBody @Validated AddProductStockRequest request) {
        return ApiResponse.successfulResponse(
                "Stock added successfully",
                addProductStockUseCase.addProductStock(request)
        );
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<?> getProductMutations(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer length,
            @RequestParam(defaultValue = "createdAt") String field,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) String search,
            @PathVariable Long warehouseId
    ) {
        return ApiResponse.successfulResponse(
                "Products retrieved successfully",
                Pagination.mapResponse(getProductMutationUseCase
                        .getProductMutations(
                                Pagination.createPageable(start, length, field, order),
                                search,
                                warehouseId))
        );
    }

    @PostMapping("create-mutation")
    public ResponseEntity<?> createMutation(@RequestBody @Validated InternalProductMutationRequest request) {
        return ApiResponse.successfulResponse(
                "Internal mutation created successfully",
                createProductMutationUseCase.createInternalProductMutation(request)
        );
    }

    @PostMapping("cancel/{id}")
    public ResponseEntity<?> cancelMutation(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Internal mutation canceled successfully",
                rejectOrCancelProductMutationUseCase.cancelProductMutation(id)
        );
    }

    @PostMapping("reject/{id}")
    public ResponseEntity<?> rejectMutation(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Internal mutation rejected successfully",
                rejectOrCancelProductMutationUseCase.rejectProductMutation(id)
        );
    }
}
