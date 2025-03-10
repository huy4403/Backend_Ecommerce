package com.backend_ecommerce.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseData<T> {
    private int status;
    private String message;
    private T data;
}
