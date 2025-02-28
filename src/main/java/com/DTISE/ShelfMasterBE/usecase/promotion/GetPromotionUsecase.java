package com.DTISE.ShelfMasterBE.usecase.promotion;

import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetPromotionUsecase {
    Page<PromotionResponse> getPromotions(Pageable pageable, String search);
    List<PromotionResponse> getSimplePromotions();
    PromotionResponse getPromotion(Long id);
}
