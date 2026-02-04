package com.banco.banking.dto;

import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nome,
        String cpf,
        String email
) {
}
