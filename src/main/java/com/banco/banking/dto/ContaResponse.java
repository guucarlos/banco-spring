package com.banco.banking.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ContaResponse(
        UUID id,
        String numeroConta,
        BigDecimal saldo,
        boolean ativa,
        UUID clienteId
) {
}
