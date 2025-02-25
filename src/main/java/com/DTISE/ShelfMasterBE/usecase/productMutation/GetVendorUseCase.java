package com.DTISE.ShelfMasterBE.usecase.productMutation;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetVendorUseCase {
    Page<VendorResponse> getVendors(Pageable pageable, String search);
}
