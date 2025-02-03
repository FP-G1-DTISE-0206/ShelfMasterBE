package com.DTISE.ShelfMasterBE.infrastructure.user.dto;

import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String userName;
    private String imageUrl;
    private List<RoleResponse> roles;
}
