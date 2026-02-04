package com.banco.banking.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AbrirContaRequest(

        UUID clienteId,

        @NotBlank(message = "Número da conta é obrigatório")
        String numeroConta
) {
}
