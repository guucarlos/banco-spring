package com.banco.banking.controller;

import com.banco.banking.domain.Cliente;
import com.banco.banking.dto.ClienteResponse;
import com.banco.banking.dto.ClienteRequest;
import com.banco.banking.service.ClienteService;
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
import java.util.UUID;


@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criarCliente(@RequestBody ClienteRequest request) {

        Cliente cliente = Cliente.builder()
                .nome(request.nome())
                .cpf(request.cpf())
                .email(request.email())
                .build();
        Cliente salvo = clienteService.criarCliente(cliente);

        return new ClienteResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getCpf(),
                salvo.getEmail()
        );
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarCliente(@PathVariable UUID id) {

        Cliente cliente = clienteService.buscarPorId(id);

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail()
        );
    }

    @GetMapping
    public List<ClienteResponse> listarClientes() {

        return clienteService.listarTodos()
                .stream()
                .map(cliente -> new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpf(),
                        cliente.getEmail()
                ))
                .toList();
    }



}
