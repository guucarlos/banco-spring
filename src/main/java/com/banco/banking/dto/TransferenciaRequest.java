package com.banco.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferenciaRequest(

        @NotBlank
        String contaOrigem,

        @NotBlank
        String contaDestino,

        @NotNull
        @Positive
        BigDecimal valor
) {
}
