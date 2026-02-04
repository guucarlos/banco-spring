package com.banco.banking.controller;


import com.banco.banking.domain.Conta;
import com.banco.banking.dto.AbrirContaRequest;
import com.banco.banking.dto.MovimentacaoRequest;
import com.banco.banking.dto.TransferenciaRequest;
import com.banco.banking.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContasController {

    private final ContaService contaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Conta abrirConta(@RequestBody @Valid AbrirContaRequest request){
        return contaService.abrirConta(request.clienteId(), request.numeroConta());
    }

    @PostMapping("{numeroConta}/deposito")
    public Conta depositar(@PathVariable String numeroConta,
                           @RequestBody @Valid MovimentacaoRequest request){
        return contaService.depositar(numeroConta,request.valor());
    }

    @PostMapping("/{numeroConta}/saque")
    @ResponseStatus(HttpStatus.CREATED)
    public Conta sacar(@PathVariable String numeroConta,
                       @RequestBody @Valid MovimentacaoRequest request){
        return contaService.sacar(numeroConta,request.valor());
    }

    @PostMapping("/transferencia")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferir(@RequestBody @Valid TransferenciaRequest request){
        contaService.transferir(
                request.contaOrigem(),
                request.contaDestino(),
                request.valor()
        );
    }


}
