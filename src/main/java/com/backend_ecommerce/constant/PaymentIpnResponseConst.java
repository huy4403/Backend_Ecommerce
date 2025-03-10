package com.backend_ecommerce.constant;

import com.backend_ecommerce.response.IpnResponse;

public class PaymentIpnResponseConst {
    public static final IpnResponse SUCCESS = new IpnResponse("00", "Successful");
    public static final IpnResponse INVALID_TXN_REF = new IpnResponse("99", "Invalid order reference");
    public static final IpnResponse SIGNATURE_FAILED = new IpnResponse("07", "Signature failed");
    public static final IpnResponse TRANSACTION_FAILED = new IpnResponse("01", "Transaction failed");
    public static final IpnResponse ORDER_NOT_FOUND = new IpnResponse("99", "Order not found");
    public static final IpnResponse INVALID_AMOUNT = new IpnResponse("99", "Payment amount is incorrect");
}
