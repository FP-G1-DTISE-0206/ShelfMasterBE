package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.DeleteWarehouseUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class DeleteWarehouseUsecaseImpl implements DeleteWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;

    public DeleteWarehouseUsecaseImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void deleteWarehouse(Long id) {
        try {
            Optional<Warehouse> event = warehouseRepository.findById(id);
            if (event.isEmpty()) {
                throw new RuntimeException("Warehouse not found");
            }
            Warehouse deletedWarehouse = event.get();
            deletedWarehouse.setDeletedAt(OffsetDateTime.now());
            warehouseRepository.save(deletedWarehouse);

        } catch (Exception e) {
            throw new RuntimeException("Can't delete warehouse, " + e.getMessage());
        }
    }
}
