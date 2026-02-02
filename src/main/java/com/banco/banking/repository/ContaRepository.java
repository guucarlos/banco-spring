package com.banco.banking.repository;

import com.banco.banking.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {

    Optional<Conta> findByNumeroConta(String numeroConta);
}
