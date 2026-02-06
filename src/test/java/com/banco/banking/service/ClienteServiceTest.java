package com.banco.banking.service;

import com.banco.banking.domain.Cliente;
import com.banco.banking.exception.BusinessException;
import com.banco.banking.exception.ResourceNotFoundException;
import com.banco.banking.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.module.ResolutionException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveCriarClienteComSucesso(){
        Cliente cliente = Cliente.builder()
                .nome("Gustavo")
                .cpf("41199986810")
                .email("guucarlos18@gmail.com")
                .build();

        when(clienteRepository.findByCpf(cliente.getCpf()))
                .thenReturn(Optional.empty());

        when(clienteRepository.save(cliente))
                .thenReturn(cliente);

        Cliente salvo = clienteService.criarCliente(cliente);

        assertNotNull(salvo);
        assertEquals("Gustavo", salvo.getNome());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste(){
        Cliente cliente = Cliente.builder()
                .cpf("41199986810")
                .build();

        when(clienteRepository.findByCpf(cliente.getCpf()))
                .thenReturn(Optional.of(cliente));

        assertThrows(BusinessException.class,
                () -> clienteService.criarCliente(cliente));
    }

    @Test
    void deveBuscarClientePorId(){
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.builder().id(id).build();

        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(cliente));

        Cliente encontrado = clienteService.buscarPorId(id);

        assertEquals(id, encontrado.getId());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste(){
        UUID id = UUID.randomUUID();

        when(clienteRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clienteService.buscarPorId(id));
    }
}
