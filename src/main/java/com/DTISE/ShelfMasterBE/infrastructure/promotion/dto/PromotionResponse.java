package com.DTISE.ShelfMasterBE.infrastructure.promotion.dto;

import com.DTISE.ShelfMasterBE.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String productUrl;
    public PromotionResponse(Promotion promotion) {
        this.id = promotion.getId();
        this.title = promotion.getTitle();
        this.description = promotion.getDescription();
        this.imageUrl = promotion.getImageUrl();
        this.productUrl = promotion.getProductUrl();
    }
}
