package com.DTISE.ShelfMasterBE.usecase.cart.impl;

import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CartCalculationRequest;
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
    public PaymentResponse execute(CartCalculationRequest request) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItemRequest itemRequest : request.getCartItems()){
            Optional<Product> productOpt = productRepository.findById(itemRequest.getProductId());
            if(productOpt.isPresent()){
                Product product = productOpt.get();

                // Convert quantity to BigDecimal and perform multiplication
                BigDecimal productPrice = product.getPrice();
                BigDecimal quantity = BigDecimal.valueOf(itemRequest.getQuantity());
                BigDecimal itemTotal = productPrice.multiply(quantity);

                totalAmount = totalAmount.add(itemTotal);
            } else {
                throw new RuntimeException("Product not found with ID: " + itemRequest.getProductId());
            }
        }

        // Construct the PaymentResponse
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTotalAmount(totalAmount);

        return paymentResponse;
    }

}
