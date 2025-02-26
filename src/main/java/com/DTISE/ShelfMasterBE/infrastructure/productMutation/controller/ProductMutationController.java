package com.DTISE.ShelfMasterBE.infrastructure.productMutation.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AddProductStockRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.InternalProductMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.RejectionReasonRequest;
import com.DTISE.ShelfMasterBE.usecase.productMutation.*;
import com.DTISE.ShelfMasterBE.usecase.warehouse.GetAssignedWarehouseUseCase;
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
    private final GetMutationTypeUseCase getMutationTypeUseCase;
    private final GetAssignedWarehouseUseCase getAssignedWarehouseUseCase;
    private final GetVendorUseCase getVendorUseCase;

    public ProductMutationController(
            AddProductStockUseCase addProductStockUseCase,
            GetProductMutationUseCase getProductMutationUseCase,
            CreateProductMutationUseCase createProductMutationUseCase,
            RejectOrCancelProductMutationUseCase rejectOrCancelProductMutationUseCase,
            ApproveProductMutationUseCase approveProductMutationUseCase,
            GetDetailLogsUseCase getDetailLogsUseCase,
            OrderMutationUseCase orderMutationUseCase,
            ReturnOrderMutationUseCase returnOrderMutationUseCase,
            GetMutationTypeUseCase getMutationTypeUseCase,
            GetAssignedWarehouseUseCase getAssignedWarehouseUseCase,
            GetVendorUseCase getVendorUseCase
    ) {
        this.addProductStockUseCase = addProductStockUseCase;
        this.getProductMutationUseCase = getProductMutationUseCase;
        this.createProductMutationUseCase = createProductMutationUseCase;
        this.rejectOrCancelProductMutationUseCase = rejectOrCancelProductMutationUseCase;
        this.approveProductMutationUseCase = approveProductMutationUseCase;
        this.getDetailLogsUseCase = getDetailLogsUseCase;
        this.orderMutationUseCase = orderMutationUseCase;
        this.returnOrderMutationUseCase = returnOrderMutationUseCase;
        this.getMutationTypeUseCase = getMutationTypeUseCase;
        this.getAssignedWarehouseUseCase = getAssignedWarehouseUseCase;
        this.getVendorUseCase = getVendorUseCase;
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
            @RequestParam(required = false) Long mutationTypeId,
            @PathVariable Long warehouseId
    ) {
        return ApiResponse.successfulResponse(
                "Products retrieved successfully",
                Pagination.mapResponse(getProductMutationUseCase
                        .getProductMutations(
                                Pagination.createPageable(start, length, field, order),
                                search, mutationTypeId,
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
    public ResponseEntity<?> rejectMutation(
            @RequestBody @Validated RejectionReasonRequest req,
            @PathVariable Long id
    ) {
        return ApiResponse.successfulResponse(
                "Internal mutation rejected successfully",
                rejectOrCancelProductMutationUseCase.rejectProductMutation(id, req)
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

    @GetMapping("/vendor")
    public ResponseEntity<?> getVendors(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer length,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(required = false) String search
    ) {
        return ApiResponse.successfulResponse(
                "Mutation types retrieved successfully",
                Pagination.mapResponse(getVendorUseCase
                        .getVendors(
                                Pagination.createPageable(start, length, field, order),
                                search))
        );
    }

    @GetMapping("/type")
    public ResponseEntity<?> getTypes() {
        return ApiResponse.successfulResponse(
                "Mutation types retrieved successfully",
                getMutationTypeUseCase.getAllMutationType()
        );
    }

    @GetMapping("/warehouse-list")
    public ResponseEntity<?> getAssignedWarehouse(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer length,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(required = false) String search
    ) {
        return ApiResponse.successfulResponse(
                "Assigned Warehouse retrieved successfully",
                getAssignedWarehouseUseCase.getAllAssignedWarehouse(
                        Pagination.createPageable(start, length, field, order),
                        search
                )
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
