package com.DTISE.ShelfMasterBE.infrastructure.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAdminPasswordRequest {
    private String password;
}
