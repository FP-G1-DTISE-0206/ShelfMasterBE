package com.DTISE.ShelfMasterBE.infrastructure.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VerifyEmailRequest {
    private String token;
}
