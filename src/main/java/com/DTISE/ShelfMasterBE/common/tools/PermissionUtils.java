package com.DTISE.ShelfMasterBE.common.tools;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.product.dto.AssignedWarehouseResponse;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.util.List;
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

    public static void isAdminOfCurrentWarehouse(User user, Long warehouseId, Long warehouseId2) {
        boolean isAdmin = user.getWarehouses().stream()
                .anyMatch(w -> Objects.equals(w.getId(), warehouseId) || Objects.equals(w.getId(), warehouseId2));
        if (!isAdmin) {
            throw new AuthorizationDeniedException("Unauthorized: Not an admin of current warehouse.");
        }
    }

    public static List<AssignedWarehouseResponse> getAssignedWarehouse(User user, String search) {
        return user.getWarehouses()
                .stream()
                .filter(w -> search == null || w.getName().toLowerCase().contains(search.toLowerCase()))
                .map(w -> new AssignedWarehouseResponse(w.getId(), w.getName()))
                .toList();
    }
}
