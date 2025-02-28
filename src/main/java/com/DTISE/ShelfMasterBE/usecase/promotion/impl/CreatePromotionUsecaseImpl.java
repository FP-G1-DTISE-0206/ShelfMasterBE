package com.DTISE.ShelfMasterBE.usecase.promotion.impl;

import com.DTISE.ShelfMasterBE.entity.Promotion;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionRequest;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionResponse;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.repository.PromotionRepository;
import com.DTISE.ShelfMasterBE.usecase.promotion.CreatePromotionUsecase;
import org.springframework.stereotype.Service;

@Service
public class CreatePromotionUsecaseImpl implements CreatePromotionUsecase {
    private final PromotionRepository promotionRepository;

    public CreatePromotionUsecaseImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public PromotionResponse createPromotion(PromotionRequest req) {
        try {
            Promotion createdPromotion = promotionRepository.save(req.toEntity());
            return new PromotionResponse(createdPromotion);
        } catch (Exception e) {
            throw new RuntimeException("Can't save promotion, " + e.getMessage());
        }
    }
}
