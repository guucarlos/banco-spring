package com.banco.banking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OperacaoContaRequest(
        @NotNull @Positive BigDecimal valor
        ) {}
