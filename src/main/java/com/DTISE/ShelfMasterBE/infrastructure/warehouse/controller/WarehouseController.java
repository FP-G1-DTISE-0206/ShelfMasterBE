package com.DTISE.ShelfMasterBE.infrastructure.warehouse.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.CreateWarehouseRequest;
import com.DTISE.ShelfMasterBE.usecase.warehouse.CreateWarehouseUsecase;
import com.DTISE.ShelfMasterBE.usecase.warehouse.DeleteWarehouseUsecase;
import com.DTISE.ShelfMasterBE.usecase.warehouse.EditWarehouseUsecase;
import com.DTISE.ShelfMasterBE.usecase.warehouse.GetWarehousesUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final GetWarehousesUsecase getWarehousesUsecase;
    private final CreateWarehouseUsecase createWarehouseUsecase;
    private final EditWarehouseUsecase editWarehouseUsecase;
    private final DeleteWarehouseUsecase deleteWarehouseUsecase;

    public WarehouseController(GetWarehousesUsecase getWarehousesUsecase, CreateWarehouseUsecase createWarehouseUsecase, EditWarehouseUsecase editWarehouseUsecase, DeleteWarehouseUsecase deleteWarehouseUsecase) {
        this.getWarehousesUsecase = getWarehousesUsecase;
        this.createWarehouseUsecase = createWarehouseUsecase;
        this.editWarehouseUsecase = editWarehouseUsecase;
        this.deleteWarehouseUsecase = deleteWarehouseUsecase;
    }

    @GetMapping("")
    public ResponseEntity<?> getWarehouses(@RequestParam int start,
                                           @RequestParam int length,
                                           @RequestParam(required = false) String search) {
        int page = start / length;
        Sort.Direction direction = Sort.Direction.fromString("desc");
        Sort sort = Sort.by(direction, "id");
        Pageable pageable = PageRequest.of(page, length, sort);
        Page<Warehouse> adminPage = getWarehousesUsecase.getWarehouses(pageable, search);
        Map<String, Object> response = new HashMap<>();
        response.put("recordsFiltered", adminPage.getTotalElements());
        response.put("data", adminPage.getContent());
        return ApiResponse.successfulResponse("Warehouse list retrieved successfully", response);
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createWarehouse(@RequestBody CreateWarehouseRequest req) {
        return ApiResponse.successfulResponse("Warehouse created successfully", createWarehouseUsecase.createWarehouse(req));
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWarehouse(@RequestBody CreateWarehouseRequest req, @PathVariable Long id) {
        return ApiResponse.successfulResponse("Warehouse updated successfully", editWarehouseUsecase.editWarehouse(req, id));
    }

    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        deleteWarehouseUsecase.deleteWarehouse(id);
        return ApiResponse.successfulResponse("Warehouse deleted successfully");
    }
}
