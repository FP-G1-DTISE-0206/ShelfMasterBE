package com.DTISE.ShelfMasterBE.usecase.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleAuthUsecase {
    GoogleIdToken.Payload verifyGoogleToken(String token);
}
