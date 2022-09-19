package com.serasa.desafio.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serasa.desafio.infrastructure.repository.ScoreRepository;
import com.serasa.desafio.models.domain.Score;
import com.serasa.desafio.models.dto.request.CadastroScoreRequestDTO;
import com.serasa.desafio.models.exception.CustomException;
import com.serasa.desafio.service.ScoreService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Gson gson = new Gson();

    @Value("${propriedades.json.scoreListaFallback}")
    private String diretorioJsonFallback;

    public List<Score> salvar(CadastroScoreRequestDTO cadastroScoreRequestDTO) throws Exception {
        log.info("---------------------------");
        log.info("Cadastro Score: {}", cadastroScoreRequestDTO);
        Optional<Score> scoreOptional = scoreRepository.findByDescricaoIgnoreCase(cadastroScoreRequestDTO.getScoreDescricao());
        if (scoreOptional.isPresent()) {
            log.error("Cadastro do Score '{}' já existe", cadastroScoreRequestDTO.getScoreDescricao());
            throw new CustomException(HttpStatus.CONFLICT, "Cadastro deste Score já existe.");

        }
        Score score = modelMapper.map(cadastroScoreRequestDTO, Score.class);
        log.info("Cadastro criado");
        scoreRepository.save(score);
        return scoreRepository.findAll();
    }

    public String filtrarDescricaoPorScore(Integer scorePessoa, List<Score> listaScore) throws Exception {
        log.info("---------------------------");
        log.info("Buscando Descrição para o Score: {}", scorePessoa);
        Optional<Score> scoreOptional = Score.filtrarDescricao(listaScore, scorePessoa);
        if (scoreOptional.isPresent()) {
            log.info("Descrição: '{}' para o Score: '{}'", scoreOptional.get().getDescricao(), scorePessoa);
            return scoreOptional.get().getDescricao();
        }
        log.error("Descrição não Encontrada para o Score: {}", scorePessoa);
        throw new CustomException(HttpStatus.CONFLICT, MessageFormat.format("Descrição não Encontrada para o Score: {0}", scorePessoa));
    }

    @CircuitBreaker(name = "fallbackListaScore", fallbackMethod = "listaScoreDefault")
    public List<Score> listar() throws Exception {
        log.info("---------------------------");
        List<Score> scoreLista = scoreRepository.findAll();
        log.info("Lista de scores: {}", scoreLista);
        //TODO simular utilizacao do cache
        /* Thread.sleep(2000);*/
        return scoreLista;
    }

    public List<Score> listaScoreDefault(Exception e) throws Exception {
        log.info("Assumindo resposta de fallback");
        Gson gson = new Gson();
        Path arquivoJson = Path.of(diretorioJsonFallback);
        return gson.fromJson(Files.readString(arquivoJson), new TypeToken<List<Score>>() {
        }.getType());
    }

}
