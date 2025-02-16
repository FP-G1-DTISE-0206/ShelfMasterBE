package com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import com.DTISE.ShelfMasterBE.infrastructure.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<UserResponse> admins;
    public WarehouseFullResponse(Warehouse warehouse) {
        this.id = warehouse.getId();
        this.name = warehouse.getName();
        this.contactName = warehouse.getContactName();
        this.contactNumber = warehouse.getContactNumber();
        this.province = warehouse.getProvince();
        this.city = warehouse.getCity();
        this.district = warehouse.getDistrict();
        this.postalCode = warehouse.getPostalCode();
        this.address = warehouse.getAddress();
        this.latitude = warehouse.getLatitude();
        this.longitude = warehouse.getLongitude();
        this.areaId = warehouse.getAreaId();
        this.admins = warehouse.getAdmins().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}
