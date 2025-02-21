package com.DTISE.ShelfMasterBE.infrastructure.user.dto;

import com.DTISE.ShelfMasterBE.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String userName;
    private String imageUrl;
    private List<RoleResponse> roles;
    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.imageUrl = user.getImageUrl();
        this.roles = user.getRoles().stream()
                .map(RoleResponse::new)
                .collect(Collectors.toList());
    }
}
