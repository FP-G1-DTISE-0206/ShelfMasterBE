package com.DTISE.ShelfMasterBE.infrastructure.userAddress.dto;

import com.DTISE.ShelfMasterBE.entity.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRequest {
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
    private Boolean isDefault = false;

    public UserAddress toEntity() {
        UserAddress userAddress = new UserAddress();
        userAddress.setContactName(contactName);
        userAddress.setContactNumber(contactNumber);
        userAddress.setProvince(province);
        userAddress.setCity(city);
        userAddress.setDistrict(district);
        userAddress.setPostalCode(postalCode);
        userAddress.setAddress(address);
        userAddress.setLatitude(latitude);
        userAddress.setLongitude(longitude);
        userAddress.setAreaId(areaId);
        userAddress.setIsDefault(isDefault);
        return userAddress;
    }
}
