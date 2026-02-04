package com.banco.banking.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @NotBlank(message = "Email é obrigatório" )
        String email
) {
}
