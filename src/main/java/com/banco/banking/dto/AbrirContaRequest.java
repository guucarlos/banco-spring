package com.banco.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AbrirContaRequest(
        @NotNull UUID clientId,
        @NotBlank String numeroConta
) {}
