package com.DTISE.ShelfMasterBE.infrastructure.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailRequest {
    private String newEmail;
    private String password;
}
