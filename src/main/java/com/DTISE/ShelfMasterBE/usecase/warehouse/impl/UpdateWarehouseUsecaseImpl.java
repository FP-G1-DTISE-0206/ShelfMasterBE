package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseFullResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseRequest;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.UpdateWarehouseUsecase;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UpdateWarehouseUsecaseImpl implements UpdateWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    public UpdateWarehouseUsecaseImpl(WarehouseRepository warehouseRepository, UserRepository userRepository) {
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public WarehouseFullResponse updateWarehouse(WarehouseRequest req, Long id) {
        try {
            return warehouseRepository.findById(id)
                    .map(existingWarehouse -> {
                        existingWarehouse.setName(req.getName());
                        existingWarehouse.setContactName(req.getContactName());
                        existingWarehouse.setContactNumber(req.getContactNumber());
                        existingWarehouse.setProvince(req.getProvince());
                        existingWarehouse.setCity(req.getCity());
                        existingWarehouse.setDistrict(req.getDistrict());
                        existingWarehouse.setPostalCode(req.getPostalCode());
                        existingWarehouse.setAddress(req.getAddress());
                        existingWarehouse.setLatitude(req.getLatitude());
                        existingWarehouse.setLongitude(req.getLongitude());
                        existingWarehouse.setAreaId(req.getAreaId());
                        Set<User> admins = new HashSet<>(userRepository.findAllById(req.getAdminsId()));
                        existingWarehouse.setAdmins(admins);
                        return warehouseRepository.save(existingWarehouse);
                    })
                    .map(WarehouseFullResponse::new)
                    .orElseThrow(() -> new DataNotFoundException(
                            "There's no warehouse with ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Can't update warehouse, " + e.getMessage());
        }
    }
}
