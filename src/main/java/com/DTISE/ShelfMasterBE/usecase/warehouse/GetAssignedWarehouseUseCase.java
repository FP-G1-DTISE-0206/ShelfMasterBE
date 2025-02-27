package com.DTISE.ShelfMasterBE.usecase.warehouse;

import com.DTISE.ShelfMasterBE.infrastructure.product.dto.AssignedWarehouseResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetAssignedWarehouseUseCase {
    List<AssignedWarehouseResponse> getAllAssignedWarehouse(Pageable pageable, String search);
}
