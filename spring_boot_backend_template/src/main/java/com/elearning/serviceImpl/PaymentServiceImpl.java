package com.elearning.serviceImpl;

import com.elearning.entity.Course;
import com.elearning.entity.Payment;
import com.elearning.entity.Student;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.PaymentRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    // ===============================
    // CREATE RAZORPAY ORDER
    // ===============================
    @Override
    public Map<String, Object> createOrder(Long studentId, Long courseId) {

        // 1. Fetch student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Fetch course (amount MUST come from DB)
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        int amountInPaise = (int) (course.getPrice() * 100);

        // 3. Create Razorpay order
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());

        Order order;
        try {
            order = razorpayClient.orders.create(orderRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }

        // 4. Save payment record (CREATED)
        Payment payment = Payment.builder()
                .student(student)
                .course(course)
                .razorpayOrderId(order.get("id"))
                .amount(course.getPrice())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        // 5. Return order details to frontend
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", amountInPaise);
        response.put("currency", "INR");

        return response;
    }

    // ===============================
    // VERIFY PAYMENT
    // ===============================
    @Override
    public String verifyPayment(String razorpayOrderId,
                                String razorpayPaymentId,
                                String razorpaySignature) {

        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean isValid = Utils.verifyPaymentSignature(
                    options,
                    razorpayKeySecret
            );

            if (!isValid) {
                payment.setStatus("FAILED");
                paymentRepository.save(payment);
                throw new RuntimeException("Payment verification failed");
            }

        } catch (Exception e) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new RuntimeException("Payment verification failed", e);
        }

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setStatus("SUCCESS");

        paymentRepository.save(payment);

        return "Payment verified successfully";
    }
}
