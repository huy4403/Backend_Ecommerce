package com.backend_ecommerce.controller.payment;

import com.backend_ecommerce.response.IpnResponse;
import com.backend_ecommerce.service.IpnHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final IpnHandler ipnHandler;


    //Return url
    @GetMapping("{id}/status")
    public ResponseEntity<Void> callBack(@PathVariable("id") Long id,
                                         @RequestParam("vnp_ResponseCode") String vnpResponseCode,
                                         HttpServletResponse response) throws IOException {
        String redirectUrl;

        if ("00".equals(vnpResponseCode)) {
//            paymentOrderService.updateStatus(id, PaymentOrderStatus.SUCCESS);
//            orderService.updatePaymentStatus(id, PaymentStatus.COMPLETED);
            redirectUrl = "http://127.0.0.1:5500/paymentResult.html?orderId=" + id + "&status=success";
        } else {
//            paymentOrderService.updateStatus(id, PaymentOrderStatus.FAILED);
//            orderService.updatePaymentStatus(id, PaymentStatus.FAILED);
            redirectUrl = "http://127.0.0.1:5500/paymentResult.html?orderId=" + id + "&status=failed";
        }

        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }


    //Ipn url
    @GetMapping("/ipn")
    public IpnResponse processIpn(@RequestParam Map<String, String> params) {
        log.info("VNPAY IPN: {}", params);
        return ipnHandler.processIpn(params);
    }
}
