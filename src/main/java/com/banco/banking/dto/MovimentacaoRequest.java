package com.banco.banking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MovimentacaoRequest(

        @NotNull
        @Positive(message = "Valor deve ser maior que zero")
        BigDecimal valor
) {
}
