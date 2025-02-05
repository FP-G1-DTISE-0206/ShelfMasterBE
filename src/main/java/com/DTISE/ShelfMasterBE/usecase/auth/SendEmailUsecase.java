package com.DTISE.ShelfMasterBE.usecase.auth;

public interface SendEmailUsecase {
    void sendVerificationEmail(String emailDestination, String token);
}
