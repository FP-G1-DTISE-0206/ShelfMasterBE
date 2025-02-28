package com.DTISE.ShelfMasterBE.infrastructure.promotion.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import com.DTISE.ShelfMasterBE.common.tools.Pagination;
import com.DTISE.ShelfMasterBE.infrastructure.promotion.dto.PromotionRequest;
import com.DTISE.ShelfMasterBE.usecase.promotion.CreatePromotionUsecase;
import com.DTISE.ShelfMasterBE.usecase.promotion.DeletePromotionUsecase;
import com.DTISE.ShelfMasterBE.usecase.promotion.GetPromotionUsecase;
import com.DTISE.ShelfMasterBE.usecase.promotion.UpdatePromotionUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotion")

public class PromotionController {
    private final CreatePromotionUsecase createPromotionUsecase;
    private final GetPromotionUsecase getPromotionUsecase;
    private final UpdatePromotionUsecase updatePromotionUsecase;
    private final DeletePromotionUsecase deletePromotionUsecase;

    public PromotionController(CreatePromotionUsecase createPromotionUsecase, GetPromotionUsecase getPromotionUsecase, UpdatePromotionUsecase updatePromotionUsecase, DeletePromotionUsecase deletePromotionUsecase) {
        this.createPromotionUsecase = createPromotionUsecase;
        this.getPromotionUsecase = getPromotionUsecase;
        this.updatePromotionUsecase = updatePromotionUsecase;
        this.deletePromotionUsecase = deletePromotionUsecase;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    public ResponseEntity<?> getPromotions(@RequestParam int start,
                                           @RequestParam int length,
                                           @RequestParam(required = false) String search,
                                           @RequestParam(required = false) String field,
                                           @RequestParam(required = false) String order) {
        return ApiResponse.successfulResponse(
                "Promotion list retrieved successfully",
                Pagination.mapResponse(getPromotionUsecase
                        .getPromotions(
                                Pagination.createPageable(start, length, field, order),
                                search))
        );
    }

    @GetMapping("/simple")
    public ResponseEntity<?> getSimplePromotions() {
        return ApiResponse.successfulResponse("Simple promotion list retrieved successfully", getPromotionUsecase.getSimplePromotions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    public ResponseEntity<?> getPromotion(@PathVariable Long id) {
        return ApiResponse.successfulResponse("Promotion retrieved successfully", getPromotionUsecase.getPromotion(id));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    public ResponseEntity<?> createPromotion(@RequestBody @Validated PromotionRequest req) {
        return ApiResponse.successfulResponse("Promotion created successfully", createPromotionUsecase.createPromotion(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    public ResponseEntity<?> updatePromotion(@RequestBody @Validated PromotionRequest req, @PathVariable Long id) {
        return ApiResponse.successfulResponse("Promotion updated successfully", updatePromotionUsecase.updatePromotion(req, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_SUPER_ADMIN')")
    public ResponseEntity<?> deletePromotion(@PathVariable Long id) {
        deletePromotionUsecase.deletePromotion(id);
        return ApiResponse.successfulResponse("Promotion deleted successfully");
    }
}
