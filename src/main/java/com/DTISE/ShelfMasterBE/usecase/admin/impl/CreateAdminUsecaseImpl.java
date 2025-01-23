package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.AdminRegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.RoleRepository;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.warehouse.repository.WarehouseRepository;
import com.DTISE.ShelfMasterBE.usecase.admin.CreateAdminUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public RegisterResponse createAdmin(AdminRegisterRequest req) {
        User newUser = req.toEntity();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        newUser.setRoles(roles);
        Optional<Role> defaultRole;
        defaultRole = roleRepository.findByName("WH_ADMIN");
        if(defaultRole.isEmpty()){
            throw new RuntimeException("Default role not found");
        }
        newUser.getRoles().add(defaultRole.get());

        Optional<Warehouse> warehouse = warehouseRepository.findById(req.getWarehouseId());
        if(warehouse.isEmpty()){
            throw new RuntimeException("Warehouse not found");
        }
        newUser.getWarehouses().add(warehouse.get());

        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Can't save user, " + e.getMessage());
        }
        return new RegisterResponse(newUser.getId(),newUser.getEmail());
    }
}
