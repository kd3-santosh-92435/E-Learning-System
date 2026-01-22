package com.elearning.service;

import java.util.Map;

public interface PaymentService {

    Map<String, Object> createOrder(Long studentId, Long courseId);

    String verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    );
}
