package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.AutoMutationResponse;
import com.DTISE.ShelfMasterBE.usecase.productMutation.AutoMutationUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutoMutationUseCaseImpl implements AutoMutationUseCase {
    @Override
    @Transactional
    public AutoMutationResponse autoMutate(AutoMutationRequest request) {
        //need current warehouse near order shipment address
        //to know need mutation type WAREHOUSE to WAREHOUSE or not
        return null;
    }
}
