package com.DTISE.ShelfMasterBE.common.tools;

import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.RoleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserRoleMapper {
    public static List<RoleResponse> mapUserRoleResponse(Set<Role> roles) {
        List<RoleResponse> responses = new ArrayList<>();
        for (Role role : roles) {
            responses.add(new RoleResponse(role.getId(), role.getName()));
        }
        return responses;
    }
}
