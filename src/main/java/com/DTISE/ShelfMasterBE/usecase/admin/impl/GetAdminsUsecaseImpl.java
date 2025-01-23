package com.DTISE.ShelfMasterBE.usecase.admin.impl;

import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.admin.GetAdminsUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class GetAdminsUsecaseImpl implements GetAdminsUsecase {

    private final UserRepository userRepository;

    public GetAdminsUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getAdmins(Pageable pageable, String search) {
        return userRepository.findAdminsBySearch( search, pageable);
    }
}
