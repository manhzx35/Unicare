package org.example.unicare_backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.unicare_backend.models.Payment;
import org.example.unicare_backend.models.Subscription;
import org.example.unicare_backend.repositories.PaymentRepository;
import org.example.unicare_backend.repositories.SubscriptionRepository;
import org.example.unicare_backend.security.services.UserDetailsImpl;
import org.example.unicare_backend.services.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private VNPayService vnpayService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestParam long amount, @RequestParam String planName) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();
        String txnRef = UUID.randomUUID().toString();
        String orderInfo = "Payment for plan " + planName;

        String paymentUrl = vnpayService.createPaymentUrl(amount, orderInfo, txnRef);

        Payment payment = Payment.builder()
                .userId(userId)
                .amount(amount)
                .orderInfo(orderInfo)
                .vnpTxnRef(txnRef)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-callback")
    @Transactional
    public ResponseEntity<?> vnpayCallback(HttpServletRequest request) {
        int result = vnpayService.orderReturn(request);
        String txnRef = request.getParameter("vnp_TxnRef");
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");

        Payment payment = paymentRepository.findByVnpTxnRef(txnRef)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setVnpResponseCode(vnp_ResponseCode);
        payment.setVnpTransactionNo(vnp_TransactionNo);
        payment.setUpdatedAt(LocalDateTime.now());

        if (result == 1) {
            payment.setStatus("SUCCESS");
            
            // Activate subscription
            String planName = payment.getOrderInfo().replace("Payment for plan ", "");
            Subscription subscription = subscriptionRepository.findByUserIdAndActive(payment.getUserId(), true)
                    .orElse(Subscription.builder()
                            .userId(payment.getUserId())
                            .startDate(LocalDateTime.now())
                            .active(true)
                            .build());
            
            subscription.setPlanName(planName);
            // Set end date based on plan (e.g., 30 days for month)
            subscription.setEndDate(LocalDateTime.now().plusDays(30));
            subscriptionRepository.save(subscription);
            
            paymentRepository.save(payment);
            return ResponseEntity.ok("Payment Success");
        } else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            return ResponseEntity.status(400).body("Payment Failed");
        }
    }
}
