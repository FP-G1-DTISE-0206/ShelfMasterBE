package com.DTISE.ShelfMasterBE.usecase.user.impl;

import com.DTISE.ShelfMasterBE.entity.Role;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterRequest;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.RegisterResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.RoleRepository;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.user.CreateUserUsecase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CreateUserUsecaseImpl implements CreateUserUsecase {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUsecaseImpl(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse createUser(RegisterRequest req) {
        User newUser = req.toEntity();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        newUser.setRoles(roles);
        Optional<Role> defaultRole;
        defaultRole = roleRepository.findByName("USER");
        if(defaultRole.isEmpty()){
            throw new RuntimeException("Default role not found");
        }
        newUser.getRoles().add(defaultRole.get());
        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Can't save user, " + e.getMessage());
        }
        return new RegisterResponse(newUser.getId(),newUser.getEmail());
    }

}
