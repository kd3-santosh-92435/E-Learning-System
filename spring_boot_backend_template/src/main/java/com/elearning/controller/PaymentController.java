package com.elearning.controller;

import com.elearning.service.PaymentService;
import com.elearning.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student/payment")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order/{courseId}")
    public ResponseEntity<Map<String, Object>> createOrder(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                paymentService.createOrder(courseId)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature) {

        return ResponseEntity.ok(
                paymentService.verifyAndEnroll(
                        razorpayOrderId,
                        razorpayPaymentId,
                        razorpaySignature
                )
        );
    }
}
