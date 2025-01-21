package com.DTISE.ShelfMasterBE.infrastructure.auth.dto;

import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String userName;
    private String password;

    public User toEntity() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUserName(userName);
        return user;
    }
}
