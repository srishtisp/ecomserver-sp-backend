package com.example.demo.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.repo.PaymentRepo;
import com.example.demo.service.RazorpayPaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentServiceImpl implements RazorpayPaymentService {

    private final PaymentRepo paymentRepo;
    private final WebClient webClient;

    @Value("${razorpay.keyId}")
    private String keyId;

    @Value("${razorpay.keySecret}")
    private String keySecret;

    @Value("${services.order}")
    private String orderBaseUrl;

    @Override
    public RazorpayCreateResponse createRazorpayOrder(String username,
                                                     String authorizationHeader,
                                                     Long orderId) throws Exception {

        InternalOrderDTO internal = webClient.get()
                .uri(orderBaseUrl + "/orders/internal/{id}", orderId)
                .header("Authorization", authorizationHeader == null ? "" : authorizationHeader)
                .retrieve()
                .bodyToMono(InternalOrderDTO.class)
                .block();

        if (internal == null) throw new RuntimeException("Order not found: " + orderId);
        if (internal.isPaid()) throw new RuntimeException("Order already paid: " + orderId);

        BigDecimal amountInr = internal.getOrderValue();
        long amountPaise = amountInr.multiply(BigDecimal.valueOf(100)).longValueExact();

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", amountPaise);
        options.put("currency", "INR");
        options.put("receipt", "rcpt_" + orderId + "_" + UUID.randomUUID().toString().substring(0, 8));

        Order rzpOrder = client.orders.create(options);

        return RazorpayCreateResponse.builder()
                .keyId(keyId)
                .razorpayOrderId(rzpOrder.get("id"))
                .orderId(orderId)
                .amount(amountInr)
                .amountPaise(amountPaise)
                .currency("INR")
                .build();
    }

    @Override
    public PaymentResponse verifyAndPay(String username,
                                        String authorizationHeader,
                                        RazorpayVerifyRequest req) {

        // ✅ TEMPORARY for testing (set true)
        boolean paymentSuccess = true;

        // If you want real verification later, use:
        // String data = req.getRazorpayOrderId() + "|" + req.getRazorpayPaymentId();
        // String expectedSignature = hmacSha256Hex(data, keySecret);
        // boolean paymentSuccess = expectedSignature.equals(req.getRazorpaySignature());

        InternalOrderDTO internalOrder = webClient.get()
                .uri(orderBaseUrl + "/orders/internal/{id}", req.getOrderId())
                .header("Authorization", authorizationHeader == null ? "" : authorizationHeader)
                .retrieve()
                .bodyToMono(InternalOrderDTO.class)
                .block();

        if (internalOrder == null) {
            throw new RuntimeException("Order not found: " + req.getOrderId());
        }

        Payment payment = Payment.builder()
                .orderId(req.getOrderId())
                .userId(internalOrder.getUserId())
                .amount(internalOrder.getOrderValue())
                .mode("RAZORPAY")
                .status(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .paymentDate(LocalDateTime.now())
                .referenceId(req.getRazorpayPaymentId())
                .build();

        payment = paymentRepo.save(payment);

        if (paymentSuccess) {
            webClient.put()
                    .uri(orderBaseUrl + "/orders/" + req.getOrderId() + "/internal/markPaid")
                    .header("Authorization", authorizationHeader == null ? "" : authorizationHeader)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .mode(payment.getMode())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .referenceId(payment.getReferenceId())
                .build();
    }

    private String hmacSha256Hex(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKey);

            byte[] rawHmac = mac.doFinal(data.getBytes());
            return Hex.encodeHexString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Error while verifying Razorpay signature", e);
        }
    }
}