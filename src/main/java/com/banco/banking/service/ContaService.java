package com.banco.banking.service;

import com.banco.banking.domain.Cliente;
import com.banco.banking.domain.Conta;
import com.banco.banking.domain.TipoTransacao;
import com.banco.banking.domain.Transacao;
import com.banco.banking.exception.BusinessException;
import com.banco.banking.exception.ResourceNotFoundException;
import com.banco.banking.repository.ClienteRepository;
import com.banco.banking.repository.ContaRepository;
import com.banco.banking.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;

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

        Conta contaSalva = contaRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .valor(valor)
                .tipo(TipoTransacao.DEPOSITO)
                .contaDestino(contaSalva)
                .build();

        transacaoRepository.save(transacao);

        return contaSalva;
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
        Conta contaSalva = contaRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .valor(valor)
                .tipo(TipoTransacao.SAQUE)
                .contaOrigem(contaSalva)
                .build();

        transacaoRepository.save(transacao);

        return contaSalva;
    }

    private Conta buscarContaAtiva(String numeroConta) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
        if (!conta.getAtiva()) {
            throw new BusinessException("Conta está inativa");
        }
        return conta;
    }

    @Transactional
    public void transferir(String contaOrigemNumero,
                           String contaDestinoNumero,
                           BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor da transferência deve ser maior que zero");
        }

        Conta origem = buscarContaAtiva(contaOrigemNumero);
        Conta destino = buscarContaAtiva(contaDestinoNumero);

        if (origem.getSaldo().compareTo(valor) < 0) {
            throw new BusinessException("Saldo insuficiente para transferência");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        contaRepository.save(origem);
        contaRepository.save(destino);

        Transacao transacao = Transacao.builder()
                .valor(valor)
                .tipo(TipoTransacao.TRANSFERENCIA)
                .contaOrigem(origem)
                .contaDestino(destino)
                .build();

        transacaoRepository.save(transacao);
    }

    public List<Conta> listarContas() {
        return contaRepository.findAll();
    }
}
