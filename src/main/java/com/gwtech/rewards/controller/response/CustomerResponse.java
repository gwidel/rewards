package com.gwtech.rewards.controller.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerResponse {
    private Long id;
    private String name;
}
