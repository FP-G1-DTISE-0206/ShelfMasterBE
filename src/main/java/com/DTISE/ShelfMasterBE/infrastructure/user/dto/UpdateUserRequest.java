package com.DTISE.ShelfMasterBE.infrastructure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String userName;
    private String imageUrl;
}
