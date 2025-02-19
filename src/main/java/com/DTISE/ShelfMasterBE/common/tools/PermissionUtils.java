package com.DTISE.ShelfMasterBE.common.tools;

import com.DTISE.ShelfMasterBE.entity.User;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.util.Objects;

public class PermissionUtils {
    public static boolean isSuperAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("SUPER_ADMIN"));
    }

    public static void isAdminOfCurrentWarehouse(User user, Long warehouseId) {
        boolean isAdmin = user.getWarehouses().stream()
                .anyMatch(w -> Objects.equals(w.getId(), warehouseId));
        if (!isAdmin) {
            throw new AuthorizationDeniedException("Unauthorized: Not an admin of current warehouse.");
        }
    }
}
