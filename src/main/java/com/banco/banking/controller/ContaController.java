package com.banco.banking.controller;

import com.banco.banking.domain.Conta;
import com.banco.banking.dto.AbrirContaRequest;
import com.banco.banking.dto.ContaResponse;
import com.banco.banking.dto.OperacaoContaRequest;
import com.banco.banking.dto.TransferenciaRequest;
import com.banco.banking.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaResponse abrirConta(@RequestBody @Valid AbrirContaRequest request) {

        Conta conta = contaService.abrirConta(
                request.clientId(),
                request.numeroConta()
        );

        return toResponse(conta);
    }

    @PostMapping("/{numeroConta}/saque")
    public ContaResponse sacar(
            @PathVariable String numeroConta,
            @RequestBody @Valid OperacaoContaRequest request) {

        Conta conta = contaService.sacar(numeroConta, request.valor());
        return toResponse(conta);
    }

    @PostMapping("/{numeroConta}/deposito")
    public ContaResponse depositar(
            @PathVariable String numeroConta,
            @RequestBody @Valid OperacaoContaRequest request) {

        Conta conta = contaService.depositar(numeroConta, request.valor());
        return toResponse(conta);
    }

    @PostMapping("/transferencia")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferir(@RequestBody @Valid TransferenciaRequest request) {

        contaService.transferir(
                request.contaOrigem(),
                request.contaDestino(),
                request.valor()
        );
    }

    private ContaResponse toResponse(Conta conta) {
        return new ContaResponse(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getSaldo(),
                conta.getAtiva(),
                conta.getCliente().getId()
        );
    }

    @GetMapping
    public List<ContaResponse> listarContas() {
        return contaService.listarContas()
                .stream()
                .map(this::toResponse)
                .toList();
    }

}
