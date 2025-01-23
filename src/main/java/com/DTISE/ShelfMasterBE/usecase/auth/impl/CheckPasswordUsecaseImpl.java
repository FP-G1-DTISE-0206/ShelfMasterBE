package com.DTISE.ShelfMasterBE.usecase.auth.impl;

import com.DTISE.ShelfMasterBE.usecase.auth.CheckPasswordUsecase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class CheckPasswordUsecaseImpl implements CheckPasswordUsecase {

    private final AuthenticationManager authenticationManager;

    public CheckPasswordUsecaseImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean checkPassword(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            return authentication.isAuthenticated();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Wrong password");
        }
    }
}
