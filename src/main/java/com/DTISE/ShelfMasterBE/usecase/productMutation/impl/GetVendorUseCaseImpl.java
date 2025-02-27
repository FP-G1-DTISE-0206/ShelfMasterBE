package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.VendorResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.VendorRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.GetVendorUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetVendorUseCaseImpl implements GetVendorUseCase {
    private final VendorRepository vendorRepo;

    public GetVendorUseCaseImpl(VendorRepository vendorRepository) {
        vendorRepo = vendorRepository;
    }

    @Override
    public Page<VendorResponse> getVendors(Pageable pageable, String search) {
        return vendorRepo.findVendorsBySearch(search, pageable)
                .map(vendor -> new VendorResponse(
                        vendor.getId(),
                        vendor.getName()
                ));
    }
}
