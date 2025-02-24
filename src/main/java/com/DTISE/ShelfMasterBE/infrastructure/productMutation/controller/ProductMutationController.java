package com.DTISE.ShelfMasterBE.infrastructure.productMutation.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AddProductStockRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.InternalProductMutationRequest;
import com.DTISE.ShelfMasterBE.usecase.productMutation.*;
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
    private final ApproveProductMutationUseCase approveProductMutationUseCase;
    private final GetDetailLogsUseCase getDetailLogsUseCase;
    private final OrderMutationUseCase orderMutationUseCase;
    private final ReturnOrderMutationUseCase returnOrderMutationUseCase;

    public ProductMutationController(
            AddProductStockUseCase addProductStockUseCase,
            GetProductMutationUseCase getProductMutationUseCase,
            CreateProductMutationUseCase createProductMutationUseCase,
            RejectOrCancelProductMutationUseCase rejectOrCancelProductMutationUseCase,
            ApproveProductMutationUseCase approveProductMutationUseCase,
            GetDetailLogsUseCase getDetailLogsUseCase,
            OrderMutationUseCase orderMutationUseCase,
            ReturnOrderMutationUseCase returnOrderMutationUseCase
    ) {
        this.addProductStockUseCase = addProductStockUseCase;
        this.getProductMutationUseCase = getProductMutationUseCase;
        this.createProductMutationUseCase = createProductMutationUseCase;
        this.rejectOrCancelProductMutationUseCase = rejectOrCancelProductMutationUseCase;
        this.approveProductMutationUseCase = approveProductMutationUseCase;
        this.getDetailLogsUseCase = getDetailLogsUseCase;
        this.orderMutationUseCase = orderMutationUseCase;
        this.returnOrderMutationUseCase = returnOrderMutationUseCase;
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

    @PostMapping("/create-mutation")
    public ResponseEntity<?> createMutation(@RequestBody @Validated InternalProductMutationRequest request) {
        return ApiResponse.successfulResponse(
                "Internal mutation created successfully",
                createProductMutationUseCase.createInternalProductMutation(request)
        );
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelMutation(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Internal mutation canceled successfully",
                rejectOrCancelProductMutationUseCase.cancelProductMutation(id)
        );
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectMutation(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Internal mutation rejected successfully",
                rejectOrCancelProductMutationUseCase.rejectProductMutation(id)
        );
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveMutation(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Internal mutation approved successfully",
                approveProductMutationUseCase.approveProductMutation(id)
        );
    }

    @GetMapping("/logs/{id}")
    public ResponseEntity<?> getLogs(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Logs retrieved successfully",
                getDetailLogsUseCase.getLogs(id)
        );
    }

    @PostMapping("/auto")
    public ResponseEntity<?> createAutoMutation(@RequestBody @Validated AutoMutationRequest request) {
        return ApiResponse.successfulResponse(
                "Auto mutation done successfully",
                orderMutationUseCase.orderMutateAll(request.getUserId(), request.getOrderId(), request.getWarehouseId())
        );
    }

    @PutMapping("/return/{orderId}")
    public ResponseEntity<?> returnAutoMutation(@PathVariable Long orderId) {
        return ApiResponse.successfulResponse(
                "Return auto mutation done successfully",
                returnOrderMutationUseCase.returnOrderMutateAll(orderId)
        );
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getTotalProductStock(@PathVariable Long id) {
        return ApiResponse.successfulResponse(
                "Total product stock retrieved successfully",
                orderMutationUseCase.getProductTotalStock(id)
        );
    }
}
