package com.DTISE.ShelfMasterBE.usecase.promotion.impl;

import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionResponse;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.repository.PromotionRepository;
import com.DTISE.ShelfMasterBE.usecase.promotion.GetPromotionUsecase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPromotionUsecaseImpl implements GetPromotionUsecase {
    private final PromotionRepository promotionRepository;

    public GetPromotionUsecaseImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public Page<PromotionResponse> getPromotions(Pageable pageable, String search) {
        return promotionRepository.findPromotionBySearch(search, pageable).map(PromotionResponse::new);
    }

    @Override
    public List<PromotionResponse> getSimplePromotions() {
        return promotionRepository.findAll().stream().map(PromotionResponse::new).collect(Collectors.toList());
    }

    @Override
    public PromotionResponse getPromotion(Long promotionId) {
        return promotionRepository.findById(promotionId).map(PromotionResponse::new).orElseThrow(() -> new DataNotFoundException("There's no promotion with ID: " + promotionId));
    }
}
