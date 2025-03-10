package com.backend_ecommerce.service;

import com.backend_ecommerce.response.IpnResponse;

import java.util.Map;

public interface IpnHandler {

    IpnResponse processIpn(Map<String, String> params);
}
