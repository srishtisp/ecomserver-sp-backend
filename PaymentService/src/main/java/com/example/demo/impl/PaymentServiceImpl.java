package com.example.demo.impl;

import com.example.demo.client.OrderClient;
import com.example.demo.client.SecurityClient;
import com.example.demo.dto.PayRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.repo.PaymentRepo;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final SecurityClient securityClient;
    private final OrderClient orderClient;

    private Long resolveUserId(String username) {
        var user = securityClient.getUser(username);
        if (user == null || user.userId == null) throw new RuntimeException("User not found: " + username);
        return user.userId;
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .paymentId(p.getPaymentId())
                .orderId(p.getOrderId())
                .userId(p.getUserId())
                .amount(p.getAmount())
                .mode(p.getMode())
                .status(p.getStatus())
                .paymentDate(p.getPaymentDate())
                .referenceId(p.getReferenceId())
                .build();
    }

    private boolean isSupportedMode(String mode) {
        String m = mode == null ? "" : mode.trim().toUpperCase();
        return m.equals("CARD") || m.equals("UPI") || m.equals("NETBANKING");
    }

    // Dummy payment outcome rules:
    // - If amount matches orderValue and mode is valid => SUCCESS
    // - Else => FAILED
    @Override
    public PaymentResponse pay(String username, PayRequest req) {
        Long userId = resolveUserId(username);

        if (!isSupportedMode(req.getMode())) {
            throw new RuntimeException("Invalid payment mode. Use CARD/UPI/NETBANKING");
        }

        var order = orderClient.getInternal(req.getOrderId());
        if (order == null || order.orderId == null) {
            throw new RuntimeException("Order not found: " + req.getOrderId());
        }

        // Ensure customer is paying their own order
        if (order.userId != null && !order.userId.equals(userId)) {
            throw new RuntimeException("This order does not belong to you");
        }

        if (order.paid) {
            throw new RuntimeException("Order already paid");
        }

        PaymentStatus status;
        if (req.getAmount().compareTo(order.orderValue) == 0) {
            status = PaymentStatus.SUCCESS;
        } else {
            status = PaymentStatus.FAILED;
        }

        Payment payment = Payment.builder()
                .orderId(req.getOrderId())
                .userId(userId)
                .amount(req.getAmount())
                .mode(req.getMode().trim().toUpperCase())
                .status(status)
                .paymentDate(LocalDateTime.now())
                .referenceId("PAY-" + UUID.randomUUID())
                .build();

        Payment saved = paymentRepo.save(payment);

        // On success, mark order as paid (order-service internal)
        if (status == PaymentStatus.SUCCESS) {
            orderClient.markPaid(req.getOrderId());
        }

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse status(String username, Long orderId) {
        Long userId = resolveUserId(username);

        Payment payment = paymentRepo.findTopByOrderIdOrderByPaymentIdDesc(orderId)
                .orElseThrow(() -> new RuntimeException("No payment found for orderId=" + orderId));

        if (!payment.getUserId().equals(userId)) {
            throw new RuntimeException("This payment does not belong to you");
        }

        return toResponse(payment);
    }
    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> myPayments(String username) {

        Long userId = resolveUserId(username);

        return paymentRepo.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @Override
    public PaymentResponse retry(String username, Long paymentId) {

        Long userId = resolveUserId(username);

        Payment old = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!old.getUserId().equals(userId))
            throw new RuntimeException("Not your payment");

        if (old.getStatus() == PaymentStatus.SUCCESS)
            throw new RuntimeException("Already successful");

        old.setStatus(PaymentStatus.SUCCESS);
        old.setReferenceId("PAY-" + UUID.randomUUID());

        orderClient.markPaid(old.getOrderId());

        return toResponse(paymentRepo.save(old));
    }
    @Override
    public PaymentResponse refund(String username, Long orderId) {

        Long userId = resolveUserId(username);

        Payment payment = paymentRepo.findTopByOrderIdOrderByPaymentIdDesc(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getUserId().equals(userId))
            throw new RuntimeException("Not your payment");

        if (payment.getStatus() != PaymentStatus.SUCCESS)
            throw new RuntimeException("Only successful payments can be refunded");

        Payment refund = Payment.builder()
                .orderId(orderId)
                .userId(userId)
                .amount(payment.getAmount().negate()) // negative value
                .mode(payment.getMode())
                .status(PaymentStatus.SUCCESS)
                .paymentDate(LocalDateTime.now())
                .referenceId("REFUND-" + UUID.randomUUID())
                .build();

        return toResponse(paymentRepo.save(refund));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {

        return paymentRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
}