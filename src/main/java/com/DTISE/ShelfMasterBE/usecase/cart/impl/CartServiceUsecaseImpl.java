//package com.DTISE.ShelfMasterBE.usecase.cart.impl;
//
//import com.DTISE.ShelfMasterBE.common.exceptions.DataNotFoundException;
//import com.DTISE.ShelfMasterBE.entity.Cart;
//import com.DTISE.ShelfMasterBE.entity.Product;
//import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.CreateCartItemRequest;
//import com.DTISE.ShelfMasterBE.infrastructure.cart.dto.GetCartResponse;
//import com.DTISE.ShelfMasterBE.infrastructure.cart.repository.CartRepository;
//import com.DTISE.ShelfMasterBE.infrastructure.product.repository.ProductRepository;
//import com.DTISE.ShelfMasterBE.usecase.cart.CartServiceUsecase;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//
//@Service
//public class CartServiceUsecaseImpl implements CartServiceUsecase {
//    private final CartRepository cartRepository;
//    private final ProductRepository productRepository;
//
//    public CartServiceUsecaseImpl(
//            CartRepository cartRepository,
//            ProductRepository productRepository
//    ) {
//        this.cartRepository = cartRepository;
//        this.productRepository = productRepository;
//    }
//
//    @Override
//    @Transactional
//    public GetCartResponse addToCart(CartRequest request) {
//        Product product = getProductOrThrow(request.getProduct().getId());
//
//        Cart cart = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProduct().getId())
//                .map(existingCart -> updateCartQuantity((Cart) existingCart, request.getQuantity()))
//                .orElseGet(() -> createNewCartEntry(request, product));
//
//        Cart savedCart = cartRepository.save(cart);
//        return mapCartResponse(savedCart);
//    }
//
//    private Product getProductOrThrow(Long productId) {
//        return (Product) productRepository.findById(productId)
//                .orElseThrow(() -> new DataNotFoundException("Product with ID " + productId + " does not exist."));
//    }
//
//    private Cart updateCartQuantity(Cart cart, Long additionalQuantity) {
//        cart.setQuantity(cart.getQuantity() + additionalQuantity);
//        return cart;
//    }
//
//    private Cart createNewCartEntry(CartRequest request, Product product) {
//        Cart cart = new Cart();
//        cart.setUser_id(request.getUserId());
//        cart.setProduct_id(request.getProduct());
////        cart.setUserId(request.getUserId());
////        cart.setProduct(product);
//        cart.setQuantity(request.getQuantity());
//        cart.setIsProcessed(false);
//        return cart;
//    }
//
//    private GetCartResponse mapCartResponse(Cart cart) {
//        return new GetCartResponse(
//                cart.getId(),
//                cart.getUser_id(),
//                cart.getProduct_id().getId(),
//                cart.getProduct_id().getName(),
//                cart.getProduct_id().getPrice(),
//                cart.getQuantity(),
//                cart.getProduct_id().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
//                cart.getIsProcessed(),
//                cart.getCreatedAt(),
//                cart.getUpdatedAt()
//        );
//    }
//}
