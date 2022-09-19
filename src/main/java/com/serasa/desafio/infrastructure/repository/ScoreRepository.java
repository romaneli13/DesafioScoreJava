package com.serasa.desafio.infrastructure.repository;

import com.serasa.desafio.models.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Optional<Score> findByDescricaoIgnoreCase(String scoreDescricao);

}
