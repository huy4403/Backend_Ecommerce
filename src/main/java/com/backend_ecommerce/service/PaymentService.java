package com.backend_ecommerce.service;

import com.backend_ecommerce.request.CreatePaymentRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface PaymentService {
    String createPayment(HttpServletRequest req, CreatePaymentRequest request);

    boolean verifyIpn(Map<String, String> params);
//    IpnResponse process(Map<String, String> params);

}
