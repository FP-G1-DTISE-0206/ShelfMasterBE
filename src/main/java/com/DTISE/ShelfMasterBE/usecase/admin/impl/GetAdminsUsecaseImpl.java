package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import com.DTISE.ShelfMasterBE.usecase.admin.GetAdminsUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAdminsUsecaseImpl implements GetAdminsUsecase {

    private final UserRepository userRepository;

    public GetAdminsUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponse> getAdmins(Pageable pageable, String search) {
        return userRepository.findAdminsBySearch(search, pageable).map(UserResponse::new);
    }

    @Override
    public List<UserResponse> getAdminsSearch(String search) {
        return userRepository.findAdminsBySearchSimple(search).stream().map(UserResponse::new).collect(Collectors.toList());
    }


}
