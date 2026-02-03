package com.banco.banking.service;

import com.banco.banking.domain.Cliente;
import com.banco.banking.domain.Conta;
import com.banco.banking.exception.BusinessException;
import com.banco.banking.exception.ResourceNotFoundException;
import com.banco.banking.repository.ClienteRepository;
import com.banco.banking.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    /**
     * Regra: abrir conta para um cliente existente
     */

    public Conta abrirConta(UUID clienteId, String numeroConta) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente nao encontrado"));

        boolean contaExiste = contaRepository.findByNumeroConta(numeroConta).isPresent();
        if (contaExiste) {
            throw new BusinessException("Número da conta já existe");
        }

        Conta conta = Conta.builder()
                .numeroConta(numeroConta)
                .cliente(cliente)
                .saldo(BigDecimal.ZERO)
                .ativa(true)
                .build();

        return contaRepository.save(conta);
    }

    /**
     * Regra: depósito
     */

    public Conta depositar(String numeroConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor do depósito deve ser maior que zero");
        }

        Conta conta = buscarContaAtiva(numeroConta);

        conta.setSaldo(conta.getSaldo().add(valor));
        return contaRepository.save(conta);
    }

    /**
     * Regra: saque
     */

    public Conta sacar(String numeroConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor do saque deve ser maior que zero");
        }

        Conta conta = buscarContaAtiva(numeroConta);

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new BusinessException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        return contaRepository.save(conta);
    }

    private Conta buscarContaAtiva(String numeroConta) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
        if (!conta.getAtiva()) {
            throw new BusinessException("Conta está inativa");
        }
        return conta;
    }
}
