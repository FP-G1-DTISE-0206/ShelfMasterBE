package com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto;

import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseFullResponse {
    private Long id;
    private String name;
    private String contactName;
    private String contactNumber;
    private String province;
    private String city;
    private String district;
    private String postalCode;
    private String address;
    private Double latitude;
    private Double longitude;
    private String areaId;
    private List<UserResponse> users;
}
