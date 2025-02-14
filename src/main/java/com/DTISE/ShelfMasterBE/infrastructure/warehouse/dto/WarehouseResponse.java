package com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto;

import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {
    private Long id;
    private String name;
    private List<UserResponse> users;
}
