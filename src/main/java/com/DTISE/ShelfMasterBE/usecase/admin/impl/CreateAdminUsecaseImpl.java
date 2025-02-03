package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateEmailException;
import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.AdminResponse;
import com.DTISE.ShelfMasterBE.infrastructure.admin.dto.RoleResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.AdminRegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.RoleRepository;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto.WarehouseResponse;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.admin.CreateAdminUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreateAdminUsecaseImpl implements CreateAdminUsecase {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final WarehouseRepository warehouseRepository;

    public CreateAdminUsecaseImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, WarehouseRepository warehouseRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public AdminResponse createAdmin(AdminRegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists");
        }
        User newUser = req.toEntity();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        newUser.setRoles(roles);
        Optional<Role> defaultRole;
        defaultRole = roleRepository.findByName("WH_ADMIN");
        if (defaultRole.isEmpty()) {
            throw new RuntimeException("Default role not found");
        }
        newUser.getRoles().add(defaultRole.get());

        Optional<Warehouse> warehouse = warehouseRepository.findById(req.getWarehouseId());
        if (warehouse.isEmpty()) {
            throw new RuntimeException("Warehouse not found");
        }
        newUser.getWarehouses().add(warehouse.get());

        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Can't save user, " + e.getMessage());
        }
        return new AdminResponse(newUser.getId(), newUser.getEmail(), newUser.getUserName(), newUser.getImageUrl(), mapAdminRoleResponse(newUser.getRoles()), mapAdminWarehouseResponse(newUser.getWarehouses()));
    }

    public List<RoleResponse> mapAdminRoleResponse(Set<Role> roles) {
        List<RoleResponse> responses = new ArrayList<>();
        for (Role role : roles) {
            responses.add(new RoleResponse(role.getId(), role.getName()));
        }
        return responses;
    }

    public List<WarehouseResponse> mapAdminWarehouseResponse(Set<Warehouse> warehouses) {
        List<WarehouseResponse> responses = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            responses.add(new WarehouseResponse(warehouse.getId(), warehouse.getName(), warehouse.getLocation()));
        }
        return responses;
    }
}
