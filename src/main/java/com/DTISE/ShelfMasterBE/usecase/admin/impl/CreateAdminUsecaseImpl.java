package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DuplicateEmailException;
import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.AdminRegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.RoleRepository;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
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
    public UserResponse createAdmin(AdminRegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists");
        }
        User newUser = req.toEntity();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setIsVerified(true);
        Set<Role> roles = new HashSet<>();
        newUser.setRoles(roles);
        Optional<Role> defaultRole;
        defaultRole = roleRepository.findByName("WH_ADMIN");
        if (defaultRole.isEmpty()) {
            throw new RuntimeException("Default role not found");
        }
        newUser.getRoles().add(defaultRole.get());

        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Can't save user, " + e.getMessage());
        }
        return new UserResponse(newUser);
    }
}
