package com.DTISE.ShelfMasterBE.usecase.payment.impl;

import com.DTISE.ShelfMasterBE.entity.Payment;
import com.DTISE.ShelfMasterBE.infrastructure.payment.repository.PaymentRepository;
import com.DTISE.ShelfMasterBE.usecase.payment.HandleMidtransWebhookUsecase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HandleMidtransWebhookUsecaseImpl implements HandleMidtransWebhookUsecase {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void execute(Map<String, Object> payload) {
        try {
            // Extract relevant fields from webhook payload
            String orderId = payload.get("order_id").toString();
            String transactionStatus = payload.get("transaction_status").toString();

            // Find payment record in the database
            Payment payment = paymentRepository.findByOrderId(Long.parseLong(orderId))
                    .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + orderId));

            // Update payment status based on Midtrans response
            payment.setTransactionStatus(transactionStatus);

            // If the payment is successful, mark it as paid
            if ("settlement".equals(transactionStatus)) {
                payment.getOrder().setIsPaid(true);
            } else if ("expire".equals(transactionStatus) || "cancel".equals(transactionStatus)) {
                payment.getOrder().setIsPaid(false);
            }

            // Save updated payment status
            paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("Error processing Midtrans webhook: " + e.getMessage());
        }
    }
}
