package com.serasa.desafio.service;

import com.serasa.desafio.models.domain.Score;
import com.serasa.desafio.models.dto.request.CadastroScoreRequestDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ScoreService {
    List<Score> salvar(CadastroScoreRequestDTO cadastroScoreRequestDTO) throws Exception;

    List<Score> listar() throws Exception;

    List<Score> listaScoreDefault(Exception e) throws Exception;

    Score filtrarDescricaoPorScore(Integer scorePessoa, List<Score> listaScore) throws Exception;

}