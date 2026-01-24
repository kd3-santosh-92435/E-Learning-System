package com.elearning.service;

import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.RequiredArgsConstructor;



public interface PaymentService {

    Map<String, Object> createOrder(Long courseId);

    String verifyAndEnroll(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    );
}
