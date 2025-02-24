package com.DTISE.ShelfMasterBE.usecase.warehouse.impl;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.UserAddress;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.userAddress.repository.UserAddressRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseFullResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.warehouse.FindClosestWarehouseUsecase;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FindClosestWarehouseUsecaseImpl implements FindClosestWarehouseUsecase {
    private final WarehouseRepository warehouseRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;
    private static final double EARTH_RADIUS_KM = 6371;

    public FindClosestWarehouseUsecaseImpl(WarehouseRepository warehouseRepository, UserAddressRepository userAddressRepository, UserRepository userRepository) {
        this.warehouseRepository = warehouseRepository;
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public WarehouseFullResponse findClosestWarehouse(String email,Long userAddressId) {
        User user=userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        UserAddress userAddress = userAddressRepository.findByIdAndUser_Id(userAddressId,user.getId()).orElseThrow(() -> new RuntimeException("User address not found"));
        List<Warehouse> warehouses = warehouseRepository.findAll();
        double minDistance = Double.MAX_VALUE;
        Warehouse closest = null;
        for (Warehouse warehouse : warehouses) {
            double distance = haversine(userAddress.getLatitude(), userAddress.getLongitude(), warehouse.getLatitude(), warehouse.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                closest = warehouse;
            }
        }
        assert closest != null;
        return new WarehouseFullResponse(closest);
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
