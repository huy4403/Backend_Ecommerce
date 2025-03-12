package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.constant.PaymentIpnResponseConst;
import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.domain.PaymentStatus;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.OrderItem;
import com.backend_ecommerce.repository.OrderRepository;
import com.backend_ecommerce.response.IpnResponse;
import com.backend_ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                        orderService.updatePaymentStatus(orderId, PaymentStatus.FAILED);
                        orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
                        return PaymentIpnResponseConst.INVALID_AMOUNT;
                    }
                } catch (NumberFormatException e) {
                    transactionService.updateStatus(orderId, TransactionStatus.FAILED);
                    orderService.updatePaymentStatus(orderId, PaymentStatus.FAILED);
                    orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
                    return PaymentIpnResponseConst.INVALID_AMOUNT;
                }

                transactionService.updateStatus(orderId, TransactionStatus.SUCCESS);
                orderService.updatePaymentStatus(orderId, PaymentStatus.COMPLETED);

                //Update product variant quantity

                List<OrderItem> orderItems = order.getOrderItems();

                for(OrderItem orderItem : orderItems) {
                    Long productVariantId = orderItem.getProductVariant().getId();
                    int quantity = -orderItem.getQuantity();

                    productVariantService.updateProductVariantQuantity(productVariantId, quantity);
                }

                return PaymentIpnResponseConst.SUCCESS;

            } else {
                transactionService.updateStatus(orderId, TransactionStatus.FAILED);
                orderService.updatePaymentStatus(orderId, PaymentStatus.FAILED);
                orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
                return PaymentIpnResponseConst.TRANSACTION_FAILED;
            }
        }
        catch (Exception e) {
            transactionService.updateStatus(orderId, TransactionStatus.FAILED);
            orderService.updatePaymentStatus(orderId, PaymentStatus.FAILED);
            orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
            return PaymentIpnResponseConst.TRANSACTION_FAILED;
        }
    }
}
