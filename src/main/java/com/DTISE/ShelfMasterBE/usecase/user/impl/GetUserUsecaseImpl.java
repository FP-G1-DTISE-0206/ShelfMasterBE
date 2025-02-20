package com.DTISE.ShelfMasterBE.usecase.user.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.RoleResponse;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.usecase.user.GetUserUsecase;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GetUserUsecaseImpl implements GetUserUsecase {
    private final UserRepository userRepository;

    public GetUserUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getUser(String email) {
        return userRepository.findByEmail(email).map(
                user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getUserName(),
                        user.getImageUrl(),
                        user.getRoles().stream().map(RoleResponse::new).collect(Collectors.toList())
                )).orElseThrow(
                () -> new DataNotFoundException("User not found")
        );
    }
}
