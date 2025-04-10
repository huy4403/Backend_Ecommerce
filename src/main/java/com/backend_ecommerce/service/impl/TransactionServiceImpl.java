package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.exception.PaymentException;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.Transaction;
import com.backend_ecommerce.model.User;
import com.backend_ecommerce.repository.TransactionRepository;
import com.backend_ecommerce.request.UpdateTransactionStatusRequest;
import com.backend_ecommerce.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public Long createTransaction(PaymentMethod paymentMethod, User user, Order order) {

        Transaction transaction = Transaction
                .builder()
                .paymentMethod(paymentMethod)
                .user(user)
                .order(order)
                .amount(order.getTotalPrice())
                .build();
        return transactionRepository.save(transaction).getId();
    }

    @Override
    public void updateStatus(Long orderId, TransactionStatus transactionStatus) {

        Transaction transaction = transactionRepository.findByOrderId(orderId).orElseThrow(
                () -> new PaymentException("Payment order not found")
        );

        transaction.setStatus(transactionStatus);

        transactionRepository.save(transaction);

    }
}
