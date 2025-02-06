package com.DTISE.ShelfMasterBE.infrastructure.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetupAccountRequest {
    private String token;
    private String userName;
    private String password;
}
