package com.DTISE.ShelfMasterBE.infrastructure.warehouse.dto;

import com.DTISE.ShelfMasterBE.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest {
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
    private List<Long> adminsId;
    public Warehouse toEntity() {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouse.setContactName(contactName);
        warehouse.setContactNumber(contactNumber);
        warehouse.setProvince(province);
        warehouse.setCity(city);
        warehouse.setDistrict(district);
        warehouse.setPostalCode(postalCode);
        warehouse.setAddress(address);
        warehouse.setLatitude(latitude);
        warehouse.setLongitude(longitude);
        warehouse.setAreaId(areaId);
        return warehouse;
    }
}
