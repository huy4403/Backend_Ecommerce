package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.constant.PaymentIpnResponseConst;
import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.OrderItem;
import com.backend_ecommerce.repository.OrderRepository;
import com.backend_ecommerce.response.IpnResponse;
import com.backend_ecommerce.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.backend_ecommerce.constant.PaymentParams.*;
import static com.backend_ecommerce.constant.PaymentParams.AMOUNT;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpnHandlerImpl implements IpnHandler {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final TransactionService transactionService;
    private final ProductVariantService productVariantService;
    private final EmailService emailService;

    @Override
    public IpnResponse processIpn(Map<String, String> params) {

        if(!paymentService.verifyIpn(params)) {
            log.error("[VNPay Ipn] Signature verification failed");
            return PaymentIpnResponseConst.SIGNATURE_FAILED;
        }

        Long orderId;
        try {
            orderId = Long.parseLong(params.get(TXN_REF));
        } catch (NumberFormatException e) {
            log.error("[VNPay Ipn] Invalid transaction reference format: {}", params.get(TXN_REF));
            return PaymentIpnResponseConst.INVALID_TXN_REF;
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null)
            return PaymentIpnResponseConst.ORDER_NOT_FOUND;

        String email = order.getUser().getEmail();

        //main
        try {

            String responseCode = params.get(RESPONSE_CODE);
            String transactionStatus = params.get(TRANSACTION_STATUS);

            if("00".equals(responseCode) && "00".equals(transactionStatus)) {

                String receivedAmountStr = params.get(AMOUNT);
                try {
                    Long receivedAmount = Long.parseLong(receivedAmountStr);
                    if (!receivedAmount.equals(order.getTotalPrice() * 100L)) {
                        transactionService.updateStatus(orderId, TransactionStatus.FAILED);
                        orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
                        return PaymentIpnResponseConst.INVALID_AMOUNT;
                    }
                } catch (NumberFormatException e) {
                    transactionService.updateStatus(orderId, TransactionStatus.FAILED);
                    orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
                    return PaymentIpnResponseConst.INVALID_AMOUNT;
                }

                transactionService.updateStatus(orderId, TransactionStatus.SUCCESS);

                //Update product variant quantity

                List<OrderItem> orderItems = order.getOrderItems();

                for(OrderItem orderItem : orderItems) {
                    Long productVariantId = orderItem.getProductVariant().getId();
                    int quantity = -orderItem.getQuantity();

                    productVariantService.descProductVariantQuantity(productVariantId, quantity);
                }
                if(order.getOrderStatus() == OrderStatus.CANCELLED) {
                    orderService.updateOrderStatus(orderId, OrderStatus.PENDING);
                }

                String subject = "[Đoàn Huy Ecommerce] Đặt hàng thành công";
                String text = """
                    <p>Cảm ơn quý khách đã mua hàng trên hệ thống của chúng tôi.</p>
                    <p>Mã đơn hàng của quý khách là: %s</p>
                    """.formatted(orderId);
                try {
                    emailService.notification(email, subject, text);
                    emailService.notification("huy4403nd@gmail.com",
                            "[Đoàn Huy Ecommerce] Đơn hàng mới"
                            , "Đơn hàng " + orderId + " đang chờ xử lý" );
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                return PaymentIpnResponseConst.SUCCESS;

            } else {
                transactionService.updateStatus(orderId, TransactionStatus.FAILED);
                orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);

                String subject = "[Đoàn Huy Ecommerce] Đơn hàng chưa thanh toán";
                String text = """
                    <p>Quý khách có 1 đơn hàng chưa thanh toán.</p>
                    <p>Vui lòng thực hiện theo hướng dẫn để thanh toán lại:</p>
                    <ul>
                        <li>-> Truy cập website: <a href="http://localhost:5173/">Đoàn Huy Ecommerce</a></li>
                        <li>-> Vào mục <b>Đơn mua</b></li>
                        <li>-> Chọn đơn hàng có mã: <b>%s</b></li>
                        <li>-> Nhấn <b>Thanh toán lại</b></li>
                    </ul>
                    <p>Xin cảm ơn quý khách đã mua sắm tại Đoàn Huy Ecommerce.</p>
                    """.formatted(orderId);
                try {
                    emailService.notification(email, subject, text);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                return PaymentIpnResponseConst.TRANSACTION_FAILED;
            }
        }
        catch (Exception e) {
            transactionService.updateStatus(orderId, TransactionStatus.FAILED);
            orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
            return PaymentIpnResponseConst.TRANSACTION_FAILED;
        }
    }
}
