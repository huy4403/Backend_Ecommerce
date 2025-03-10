package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.request.CreatePaymentRequest;
import com.backend_ecommerce.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.backend_ecommerce.constant.PaymentParams.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.vnpay.url}")
    public String vnp_PayUrl;

    @Value("${payment.vnpay.returnUrl}")
    public String vnp_ReturnUrl;

    @Value("${payment.vnpay.tmnCode}")
    public String vnp_TmnCode;

    @Value("${payment.vnpay.secretKey}")
    public String secretKey;

    @Value("${payment.vnpay.version}")
    public String vnp_Version;

    @Value("${payment.vnpay.command}")
    public String vnp_Command;

    @Value("${payment.vnpay.orderType}")
    public String otherType;

    @Value("${payment.vnpay.expiration}")
    public int expiration;

    public String createPayment(HttpServletRequest req, CreatePaymentRequest request) {

        long amount = request.getAmount()*100L;

        String vnp_TxnRef = String.valueOf(request.getOrderId());

        String vnp_IpAddr = getIpAddress(req);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put(VERSION, vnp_Version);
        vnp_Params.put(COMMAND, vnp_Command);
        vnp_Params.put(TMN_CODE, vnp_TmnCode);
        vnp_Params.put(AMOUNT, String.valueOf(amount));
        vnp_Params.put(CURRENCY, "VND");
        vnp_Params.put(TXN_REF, vnp_TxnRef);
        vnp_Params.put(ORDER_INFO, "Thanh toan don hang:" + vnp_TxnRef);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty())
            vnp_Params.put(LOCALE, locate);
        else
            vnp_Params.put(LOCALE, "vn");

        vnp_Params.put(IP_ADDRESS, vnp_IpAddr);
        vnp_Params.put(ORDER_TYPE, otherType);
        vnp_Params.put(RETURN_URL, String.format(vnp_ReturnUrl, vnp_TxnRef));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put(CREATED_DATE, vnp_CreateDate);

        cld.add(Calendar.MINUTE, expiration);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put(EXPIRE_DATE, vnp_ExpireDate);

        return buildPaymentUrlFromFields(vnp_Params);
    }

    public boolean verifyIpn(Map<String, String> params) {
        String reqSecureHash = params.get(SECURE_HASH);
        params.remove(SECURE_HASH);
        params.remove(SECURE_HASH_TYPE);

        StringBuilder hashPayload = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                // Build hash data
                hashPayload.append(fieldName);
                hashPayload.append('=');
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (itr.hasNext()) {
                    hashPayload.append('&');
                }
            }
        }

        String expectedSignature = hmacSHA512(secretKey, hashPayload.toString());

        return expectedSignature.equals(reqSecureHash);
    }
//
//    @Override
//    public boolean isValidPaymentRequest(HttpServletRequest request) {
//        Map<String, String> params = request.getParameterMap().entrySet().stream()
//                .filter(entry -> !"vnp_SecureHash".equals(entry.getKey()))
//                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0]));
//
//        String secretKey = "YOUR_SECRET_KEY"; // Lấy từ cấu hình VNPAY
//        String hashData = params.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .map(entry -> entry.getKey() + "=" + entry.getValue())
//                .collect(Collectors.joining("&"));
//
//        String expectedHash = hmacSHA512(secretKey, hashData);
//        String actualHash = request.getParameter("vnp_SecureHash");
//
//        return expectedHash.equals(actualHash);
//    }


    public String buildPaymentUrlFromFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(secretKey, hashData.toString());

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnp_PayUrl + "?" + queryUrl;
    }

    private String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    private static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");

            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

}
