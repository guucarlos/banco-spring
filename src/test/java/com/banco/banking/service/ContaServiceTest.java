package com.banco.banking.service;

import com.banco.banking.domain.Conta;
import com.banco.banking.domain.Transacao;
import com.banco.banking.exception.BusinessException;
import com.banco.banking.repository.ContaRepository;
import com.banco.banking.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository; // 🔥 FALTAVA ISSO

    @InjectMocks
    private ContaService contaService;

    @Test
    void deveDepositarValorNaConta() {
        String numeroConta = "123456";

        Conta conta = new Conta();
        conta.setNumeroConta(numeroConta);
        conta.setSaldo(new BigDecimal("100.00"));
        conta.setAtiva(true);

        when(contaRepository.findByNumeroConta(numeroConta))
                .thenReturn(Optional.of(conta));

        contaService.depositar(numeroConta, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), conta.getSaldo());
        verify(contaRepository).save(conta);
        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void deveSacarValorQuandoSaldoSuficiente() {
        String numeroConta = "123456";

        Conta conta = new Conta();
        conta.setNumeroConta(numeroConta);
        conta.setSaldo(new BigDecimal("200.00"));
        conta.setAtiva(true);

        when(contaRepository.findByNumeroConta(numeroConta))
                .thenReturn(Optional.of(conta));

        contaService.sacar(numeroConta, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), conta.getSaldo());
        verify(contaRepository).save(conta);
        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        String numeroConta = "123456";

        Conta conta = new Conta();
        conta.setNumeroConta(numeroConta);
        conta.setSaldo(new BigDecimal("30.00"));
        conta.setAtiva(true);

        when(contaRepository.findByNumeroConta(numeroConta))
                .thenReturn(Optional.of(conta));

        assertThrows(BusinessException.class,
                () -> contaService.sacar(numeroConta, new BigDecimal("50.00")));

        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());
    }

    @Test
    void deveTransferirValorEntreContas() {
        String contaOrigemNumero = "111111";
        String contaDestinoNumero = "222222";

        Conta origem = new Conta();
        origem.setNumeroConta(contaOrigemNumero);
        origem.setSaldo(new BigDecimal("300.00"));
        origem.setAtiva(true);

        Conta destino = new Conta();
        destino.setNumeroConta(contaDestinoNumero);
        destino.setSaldo(new BigDecimal("100.00"));
        destino.setAtiva(true);

        when(contaRepository.findByNumeroConta(contaOrigemNumero))
                .thenReturn(Optional.of(origem));
        when(contaRepository.findByNumeroConta(contaDestinoNumero))
                .thenReturn(Optional.of(destino));

        contaService.transferir(contaOrigemNumero, contaDestinoNumero, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("250.00"), origem.getSaldo());
        assertEquals(new BigDecimal("150.00"), destino.getSaldo());

        verify(contaRepository).save(origem);
        verify(contaRepository).save(destino);
        verify(transacaoRepository).save(any(Transacao.class));
    }
}