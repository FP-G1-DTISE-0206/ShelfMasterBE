package com.DTISE.ShelfMasterBE.infrastructure.admin.dto;


import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponse {
    private Long id;
    private String email;
    private String userName;
    private String imageUrl;
    private List<RoleResponse> roles;
    private List<WarehouseResponse> warehouses;
}
