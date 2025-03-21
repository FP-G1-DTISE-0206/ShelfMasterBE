package com.DTISE.ShelfMasterBE.infrastructure.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;
}
