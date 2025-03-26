package com.backend_ecommerce.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DashboardResponse {
    private Long customer;
    private Long product;
    private Long category;
    private Long order;
}
