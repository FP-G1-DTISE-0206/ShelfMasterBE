package com.DTISE.ShelfMasterBE.usecase.user.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.UserRoleMapper;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.usecase.user.GetUserUsecase;
import org.springframework.stereotype.Service;

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
                        UserRoleMapper.mapUserRoleResponse(user.getRoles())
                )).orElseThrow(
                () -> new DataNotFoundException("User not found")
        );
    }
}
