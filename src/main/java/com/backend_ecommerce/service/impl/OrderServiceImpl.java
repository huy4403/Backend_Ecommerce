package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.BusinessException;
import com.backend_ecommerce.exception.OrderException;
import com.backend_ecommerce.exception.ProductException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.*;
import com.backend_ecommerce.repository.*;
import com.backend_ecommerce.request.CreateOrderRequest;
import com.backend_ecommerce.request.CreatePaymentRequest;
import com.backend_ecommerce.response.*;
import com.backend_ecommerce.service.OrderItemService;
import com.backend_ecommerce.service.OrderService;
import com.backend_ecommerce.service.TransactionService;
import com.backend_ecommerce.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest req, HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User currentUser = userPrincipal.user();

        Long cartId = userPrincipal.user().getCart().getId();

        if (cartId == null) throw new ResourceNotFoundException("Cart not found");

        Address address = addressRepository.findByIdAndUserId(req.getAddressId(), currentUser.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Address not found")
        );

        List<CartItem> cartItems = req.getCartItemIds().stream()
                .map(cartItemId -> cartItemRepository.findByIdAndCartId(cartItemId, cartId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cart item id: " + cartItemId + " not found"))
                )
                .peek(cartItem -> {

                    if(cartItem.getProductVariant().getProduct().getStatus() != ProductStatus.ACTIVE) {
                        throw new ProductException("Product id: " + cartItem.getProductVariant().getProduct().getId() + " is not active");
                    }

                    if (cartItem.getQuantity() > cartItem.getProductVariant().getQuantity()) {
                        throw new BusinessException("Cart id: " + cartItem.getId() + " has exceeded the stock quantity.");
                    }
                }).toList();

        int totalItem = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        long totalPrice = cartItems.stream().mapToLong(cartItem ->
                cartItem.getProductVariant().getProduct().getPrice() * cartItem.getQuantity()).sum();

        Order order = Order
                .builder()
                .user(currentUser)
                .totalItem(totalItem)
                .totalPrice(totalPrice)
                .shippingAddress(address)
                .build();

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            orderItemService.addOrderItem(cartItem, savedOrder);
        }

        CreateOrderResponse createOrderResponse = CreateOrderResponse
                .builder()
                .orderId(savedOrder.getId())
                .userId(currentUser.getId())
                .totalPrice(totalPrice)
                .paymentMethod(req.getPaymentMethod())
                .build();

        transactionService.createTransaction(req.getPaymentMethod(), currentUser, savedOrder);

        if (req.getPaymentMethod().equals(PaymentMethod.COD)) {
            return createOrderResponse;
        }

        CreatePaymentRequest createPaymentRequest = CreatePaymentRequest.builder()
                .orderId(savedOrder.getId())
                .amount(totalPrice)
                .build();
        String paymentUrl = paymentService.createPayment(httpServletRequest, createPaymentRequest);

        createOrderResponse.setPaymentUrl(paymentUrl);

        return createOrderResponse;
    }

    @Override
    @Transactional
    public Long updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );

        OrderStatus oldStatus = order.getOrderStatus();

        if(order.getTransaction().getPaymentMethod().equals(PaymentMethod.COD)) {
            if(orderStatus.equals(OrderStatus.DELIVERED)) {
                order.getOrderItems().forEach(orderItem -> {
                    ProductVariant product = orderItem.getProductVariant();
                    if(product.getQuantity() > orderItem.getQuantity()) {
                        productVariantRepository.decreaseStock(product.getId(), orderItem.getQuantity());
                    } else {
                        throw new IllegalArgumentException("Số lượng hàng không đủ để xác nhận đơn hàng!");
                    }
                });
            } else if(oldStatus.equals(OrderStatus.DELIVERED)) {
                order.getOrderItems().forEach(orderItem -> {
                    ProductVariant product = orderItem.getProductVariant();
                    productVariantRepository.increaseStock(product.getId(), orderItem.getQuantity());
                });
            }
        }
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order).getId();
    }

    @Override
    public List<OrderResponse> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );
        return OrderDetailResponse.mapFrom(order);
    }

    @Override
    public OrderDetailResponse getCurrentOrderById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Order order = orderRepository.findByIdAndUser(id, userPrincipal.user()).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );
        return OrderDetailResponse.mapFrom(order);
    }

    @Override
    public List<UserOrdersResponse> getUserOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        List<Order> orders = orderRepository.findAllByUser(userPrincipal.user());
        return orders.stream().map(UserOrdersResponse::mapFrom).collect(Collectors.toList());
    }

    @Override
    public RePaymentResponse rePayment(Long id, HttpServletRequest httpServletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User currentUser = userPrincipal.user();
        Order order = orderRepository.findByIdAndUser(id, currentUser).orElseThrow(
                () -> new OrderException("Order not found")
        );

        CreatePaymentRequest createPaymentRequest = CreatePaymentRequest.builder()
                .orderId(order.getId())
                .amount(order.getTotalPrice())
                .build();
        String paymentUrl = paymentService.createPayment(httpServletRequest, createPaymentRequest);

        return RePaymentResponse
                .builder()
                .orderId(order.getId())
                .paymentUrl(paymentUrl)
                .build();

    }
}
