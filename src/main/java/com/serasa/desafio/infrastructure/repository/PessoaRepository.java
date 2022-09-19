package com.serasa.desafio.infrastructure.repository;

import com.serasa.desafio.models.domain.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByNomeIgnoreCase(String nome);
}
