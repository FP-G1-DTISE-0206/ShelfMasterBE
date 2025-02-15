package com.DTISE.ShelfMasterBE.common.tools;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;

public class UserMapper {
    public static UserResponse mapUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getImageUrl(),
                UserRoleMapper.mapUserRoleResponse(user.getRoles())
        );
    }
}
