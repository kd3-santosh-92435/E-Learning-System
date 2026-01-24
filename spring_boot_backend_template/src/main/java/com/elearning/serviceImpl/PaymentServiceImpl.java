package com.elearning.serviceImpl;

import com.elearning.entity.Course;
import com.elearning.entity.Enrollment;
import com.elearning.entity.Payment;
import com.elearning.entity.Student;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.PaymentRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.service.EmailService;
import com.elearning.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final EnrollmentRepository enrollmentRepository;
    private final EmailService emailService;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    // ===============================
    // CREATE ORDER
    // ===============================
    @Override
    public Map<String, Object> createOrder(Long courseId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        int amountInPaise = (int) (course.getPrice() * 100);

        JSONObject request = new JSONObject();
        request.put("amount", amountInPaise);
        request.put("currency", "INR");
        request.put("receipt", "rcpt_" + System.currentTimeMillis());

        Order order;
        try {
            order = razorpayClient.orders.create(request);
        } catch (Exception e) {
            throw new RuntimeException("Razorpay order failed", e);
        }

        Payment payment = Payment.builder()
                .student(student)
                .course(course)
                .razorpayOrderId(order.get("id"))
                .amount(course.getPrice())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", amountInPaise);
        response.put("currency", "INR");
        response.put("courseTitle", course.getTitle());

        return response;
    }

    // ===============================
    // VERIFY → ENROLL → EMAIL
    // ===============================
    @Override
    public String verifyAndEnroll(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean isValid = Utils.verifyPaymentSignature(
                    options, razorpayKeySecret);

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

        // 🔐 Prevent duplicate enrollment
        if (!enrollmentRepository
                .existsByStudent_StudentIdAndCourse_CourseId(
                        payment.getStudent().getStudentId(),
                        payment.getCourse().getCourseId())) {

            enrollmentRepository.save(
                    Enrollment.builder()
                            .student(payment.getStudent())
                            .course(payment.getCourse())
                            .build()
            );

            emailService.sendCourseEnrollmentEmail(
                    payment.getStudent().getEmail(),
                    payment.getStudent().getName(),
                    payment.getCourse().getTitle()
            );
        }

        return "Payment successful & course enrolled";
    }

}
