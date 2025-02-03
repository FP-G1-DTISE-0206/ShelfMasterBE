package com.DTISE.ShelfMasterBE.infrastructure.warehouse.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.CreateWarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.UpdateWarehouseRequest;
import com.DTISE.ShelfMasterBE.usecase.warehouse.CreateWarehouseUsecase;
import com.DTISE.ShelfMasterBE.usecase.warehouse.DeleteWarehouseUsecase;
import com.DTISE.ShelfMasterBE.usecase.warehouse.UpdateWarehouseUsecase;
import com.DTISE.ShelfMasterBE.usecase.warehouse.GetWarehousesUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final GetWarehousesUsecase getWarehousesUsecase;
    private final CreateWarehouseUsecase createWarehouseUsecase;
    private final UpdateWarehouseUsecase updateWarehouseUsecase;
    private final DeleteWarehouseUsecase deleteWarehouseUsecase;

    public WarehouseController(GetWarehousesUsecase getWarehousesUsecase, CreateWarehouseUsecase createWarehouseUsecase, UpdateWarehouseUsecase updateWarehouseUsecase, DeleteWarehouseUsecase deleteWarehouseUsecase) {
        this.getWarehousesUsecase = getWarehousesUsecase;
        this.createWarehouseUsecase = createWarehouseUsecase;
        this.updateWarehouseUsecase = updateWarehouseUsecase;
        this.deleteWarehouseUsecase = deleteWarehouseUsecase;
    }

    @GetMapping("")
    public ResponseEntity<?> getWarehouses(@RequestParam int start,
                                           @RequestParam int length,
                                           @RequestParam(required = false) String search,
                                           @RequestParam(required = false) String field,
                                           @RequestParam(required = false) String order) {
        return ApiResponse.successfulResponse(
                "Warehouse list retrieved successfully",
                Pagination.mapResponse(getWarehousesUsecase
                        .getWarehouses(
                                Pagination.createPageable(start, length, field, order),
                                search))
        );
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createWarehouse(@RequestBody CreateWarehouseRequest req) {
        return ApiResponse.successfulResponse("Warehouse created successfully", createWarehouseUsecase.createWarehouse(req));
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWarehouse(@RequestBody UpdateWarehouseRequest req, @PathVariable Long id) {
        return ApiResponse.successfulResponse("Warehouse updated successfully", updateWarehouseUsecase.updateWarehouse(req, id));
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        deleteWarehouseUsecase.deleteWarehouse(id);
        return ApiResponse.successfulResponse("Warehouse deleted successfully");
    }
}
