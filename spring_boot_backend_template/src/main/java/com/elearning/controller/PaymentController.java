package com.elearning.controller;

import com.elearning.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student/payment")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {

        return ResponseEntity.ok(
                paymentService.createOrder(studentId, courseId)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody Map<String, String> payload) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(
                        payload.get("razorpayOrderId"),
                        payload.get("razorpayPaymentId"),
                        payload.get("razorpaySignature")
                )
        );
    }
}
