package com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto;

import com.DTISE.ShelfMasterBE.entity.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponse {
    private Long id;
    private Long userId;
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
    private Boolean isDefault;

    public UserAddressResponse(UserAddress userAddress) {
        this.id = userAddress.getId();
        this.userId = userAddress.getUser().getId();
        this.contactName = userAddress.getContactName();
        this.contactNumber = userAddress.getContactNumber();
        this.province = userAddress.getProvince();
        this.city = userAddress.getCity();
        this.district = userAddress.getDistrict();
        this.postalCode = userAddress.getPostalCode();
        this.address = userAddress.getAddress();
        this.latitude = userAddress.getLatitude();
        this.longitude = userAddress.getLongitude();
        this.areaId = userAddress.getAreaId();
        this.isDefault = userAddress.getIsDefault();
    }
}
