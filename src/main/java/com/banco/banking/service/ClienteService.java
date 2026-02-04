package com.banco.banking.service;

import com.banco.banking.domain.Cliente;
import com.banco.banking.exception.BusinessException;
import com.banco.banking.exception.ResourceNotFoundException;
import com.banco.banking.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente criarCliente(Cliente cliente){
        boolean cpfJaExiste = clienteRepository.findByCpf(cliente.getCpf()).isPresent();

        if (cpfJaExiste){
            throw new BusinessException("CPF ja existe");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(UUID id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

}
