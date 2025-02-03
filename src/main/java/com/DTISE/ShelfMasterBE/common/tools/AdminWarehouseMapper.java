package com.DTISE.ShelfMasterBE.common.tools;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdminWarehouseMapper {
    public static List<WarehouseResponse> mapAdminWarehouseResponse(Set<Warehouse> warehouses) {
        List<WarehouseResponse> responses = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            responses.add(new WarehouseResponse(warehouse.getId(), warehouse.getName(), warehouse.getLocation()));
        }
        return responses;
    }
}
