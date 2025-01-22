package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.dto.UserAuth;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.usecase.auth.GetUserAuthDetailsUsecase;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetUserAuthDetailsUsecaseImpl implements GetUserAuthDetailsUsecase {
    private final UserRepository userRepository;

    public GetUserAuthDetailsUsecaseImpl(UserRepository usersRepository) {
        this.userRepository = usersRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userRepository.findByEmailContainsIgnoreCase(username).orElseThrow(() -> new DataNotFoundException("User not found with email: " + username));

        UserAuth userAuth = new UserAuth();
        userAuth.setUser(existingUser);
        return userAuth;
    }
}
