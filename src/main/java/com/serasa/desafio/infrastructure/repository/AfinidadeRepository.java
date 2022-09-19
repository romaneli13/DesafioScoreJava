package com.serasa.desafio.infrastructure.repository;

import com.serasa.desafio.models.domain.Afinidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AfinidadeRepository extends JpaRepository<Afinidade, Long> {

    Optional<Afinidade> findByRegiaoIgnoreCase(String regiao);

}
