package com.DTISE.ShelfMasterBE.usecase.productMutation.impl;

import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.dto.MutationTypeResponse;
import com.DTISE.ShelfMasterBE.infrastructure.productMutation.repository.MutationTypeRepository;
import com.DTISE.ShelfMasterBE.usecase.productMutation.GetMutationTypeUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetMutationTypeUseCaseImpl implements GetMutationTypeUseCase {
    private final MutationTypeRepository mutationTypeRepository;

    public GetMutationTypeUseCaseImpl(MutationTypeRepository mutationTypeRepository) {
        this.mutationTypeRepository = mutationTypeRepository;
    }

    @Override
    public List<MutationTypeResponse> getAllMutationType() {
        return mutationTypeRepository.findAll(Pagination.createPageable())
                .map(type -> new MutationTypeResponse(
                        type.getId(),
                        type.getOriginType(),
                        type.getDestinationType()
                )).getContent();
    }
}
