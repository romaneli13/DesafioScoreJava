package com.serasa.desafio.service;

import com.serasa.desafio.models.domain.Score;
import com.serasa.desafio.models.dto.request.CadastroScoreRequestDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.List;

public interface ScoreService {
    @CacheEvict(value = "scores", allEntries = true)
    List<Score> salvar(CadastroScoreRequestDTO cadastroScoreRequestDTO) throws Exception;

    @Cacheable("scores")
    List<Score> listar() throws Exception;

    List<Score> listaScoreDefault(Exception e) throws Exception;

    String filtrarDescricaoPorScore(Integer scorePessoa, List<Score> listaScore) throws Exception;

}