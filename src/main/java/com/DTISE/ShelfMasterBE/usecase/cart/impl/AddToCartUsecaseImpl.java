package com.DTISE.ShelfMasterBE.usecase.cart.impl;


import com.DTISE.ShelfMasterBE.entity.Cart;
import com.DTISE.ShelfMasterBE.entity.Product;
import com.DTISE.ShelfMasterBE.entity.User;
import com.DTISE.ShelfMasterBE.infrastructure.auth.repository.UserRepository;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemRequest;
import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemResponse;
import com.DTISE.ShelfMasterBE.infrastructure.cart.repository.CartRepository;
import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
import com.DTISE.ShelfMasterBE.usecase.cart.AddToCartUsecase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AddToCartUsecaseImpl implements AddToCartUsecase {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AddToCartUsecaseImpl(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserRepository userRepository){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CreateCartItemResponse execute(CreateCartItemRequest request, String email) {
       User user = userRepository.findByEmailContainsIgnoreCase(email)
               .orElseThrow(() -> new RuntimeException("User not found"));


        if (request.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        Cart cart;
        if (existingCart.isPresent()) {
            cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        } else {
            cart = request.toEntity(user, product);
        }

        cartRepository.save(cart);

        return new CreateCartItemResponse(
                cart.getId(),
                cart.getProduct().getId(),
                cart.getProduct().getName(),
                cart.getQuantity(),
                cart.getIsProcessed(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }




}
