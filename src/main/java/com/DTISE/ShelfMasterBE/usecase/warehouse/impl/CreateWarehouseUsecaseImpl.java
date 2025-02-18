package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseFullResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.CreateWarehouseUsecase;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CreateWarehouseUsecaseImpl implements CreateWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    public CreateWarehouseUsecaseImpl(WarehouseRepository warehouseRepository, UserRepository userRepository) {
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public WarehouseFullResponse createWarehouse(WarehouseRequest req) {
        try {
            Warehouse newWarehouse = req.toEntity();
            Set<User> admins = new HashSet<>(userRepository.findAllById(req.getAdminsId()));
            newWarehouse.setAdmins(admins);
            Warehouse createdWarehouse = warehouseRepository.save(newWarehouse);
            return new WarehouseFullResponse(createdWarehouse);
        } catch (Exception e) {
            throw new RuntimeException("Can't save warehouse, " + e.getMessage());
        }

    }

}
