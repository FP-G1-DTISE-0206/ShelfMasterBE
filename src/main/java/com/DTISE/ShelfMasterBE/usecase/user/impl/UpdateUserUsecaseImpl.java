package com.DTISE.ShelfMasterBE.usecase.user.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.UserRoleMapper;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UpdateUserRequest;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.usecase.user.UpdateUserUsecase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class UpdateUserUsecaseImpl implements UpdateUserUsecase {
    private final UserRepository userRepository;

    public UpdateUserUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest req, String email) {
        try {
            return userRepository.findByEmail(email).map(user -> {
                user.setUserName(req.getUserName());
                user.setImageUrl(req.getImageUrl());
                user.setUpdatedAt(OffsetDateTime.now());
                return userRepository.save(user);
            }).map(user -> new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getUserName(),
                    user.getImageUrl(),
                    UserRoleMapper.mapUserRoleResponse(user.getRoles())
            )).orElseThrow(() -> new DataNotFoundException(
                    "There's no user with email: " + email));
        } catch (Exception e) {
            throw new RuntimeException("Can't update user, " + e.getMessage());
        }

    }
}
