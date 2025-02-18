package com.DTISE.ShelfMasterBE.infrastructure.user.dto;

import com.DTISE.ShelfMasterBE.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Long id;
    private String name;
    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }
}
