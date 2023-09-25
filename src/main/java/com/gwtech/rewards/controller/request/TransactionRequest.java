package com.gwtech.rewards.controller.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gwtech.rewards.CustomDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotBlank
    String title;

    @NotNull
    @Min(0)
    BigDecimal purchaseValue;

    @NotNull
    @JsonDeserialize(using = CustomDateDeserializer.class)
    LocalDateTime purchaseAt;
}
