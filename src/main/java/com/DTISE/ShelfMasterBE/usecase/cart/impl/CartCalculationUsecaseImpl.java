package com.DTISE.ShelfMasterBE.usecase.cart.impl;

import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartCalculationRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartRequest;
import com.DTISE.ShelfMasterBE.infrastructure.payment.dto.PaymentResponse;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.cart.CartCalculationUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartCalculationUsecaseImpl implements CartCalculationUsecase {

    private final ProductRepository productRepository;


    @Override
    public PaymentResponse execute(CartRequest request) {
//        return null;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartRequest.CartItemDTO itemRequest : request.getCartItems()) {
            Optional<Product> productOpt = productRepository.findById(itemRequest.getId());

            if(productOpt.isPresent()){
                Product product = productOpt.get();
                BigDecimal productPrice = product.getPrice();
                BigDecimal quantity = BigDecimal.valueOf(itemRequest.getQuantity());
                BigDecimal itemTotal = productPrice.multiply(quantity);
                totalAmount = totalAmount.add(itemTotal);
            } else {
                throw new IllegalArgumentException("Product not found with ID: " + itemRequest.getId());
            }
        }

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTotalAmount(totalAmount);
        return paymentResponse;
    }
}
