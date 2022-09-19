package com.serasa.desafio.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serasa.desafio.infrastructure.repository.AfinidadeRepository;
import com.serasa.desafio.models.domain.Afinidade;
import com.serasa.desafio.models.dto.request.CadastroAfinidadeRequestDTO;
import com.serasa.desafio.models.exception.CustomException;
import com.serasa.desafio.service.AfinidadeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AfinidadeServiceImpl implements AfinidadeService {

    @Autowired
    private AfinidadeRepository afinidadeRepository;
    @Autowired
    private ModelMapper modelMapper;

    private final Gson gson = new Gson();

    @Value("${propriedades.json.afinidadeListaFallback}")
    private String diretorioJsonFallback;

    @Cacheable("afinidades")
    public List<Afinidade> salvar(CadastroAfinidadeRequestDTO cadastroAfinidadeRequestDTO) throws Exception {
        log.info("---------------------------");
        log.info("Cadastrar Afinidade: {}", cadastroAfinidadeRequestDTO);
        Optional<Afinidade> afinidadeOptional = afinidadeRepository.findByRegiaoIgnoreCase(cadastroAfinidadeRequestDTO.getRegiao());
        if (afinidadeOptional.isPresent()) {
            log.info("Cadastro desta Regiao já existe.");
            throw new CustomException(HttpStatus.CONFLICT, "Cadastro desta Regiao já existe.");
        }
        Afinidade afinidade = modelMapper.map(cadastroAfinidadeRequestDTO, Afinidade.class);
        log.info("Afinidade criada");
        afinidadeRepository.save(afinidade);
        return listar();

    }

    public List<String> filtrarEstadosPorRegiao(String regiao, List<Afinidade> listaAfinidades) throws Exception {
        log.info("---------------------------");
        log.info("Filtrando Estados pela Regiao: {}", regiao);
        if (listaAfinidades.isEmpty()) {
            log.info("Lista de afinidades vazia");
            return new ArrayList<>();
        } else {
            Optional<Afinidade> afinidadeOptional = Afinidade.filtrarEstados(regiao, listaAfinidades);
            if (afinidadeOptional.isPresent()) {
                log.info("Estados: {}", afinidadeOptional.get().getEstados());
                return afinidadeOptional.get().getEstados();
            } else {
                return new ArrayList<>();
            }
        }
    }

    @CircuitBreaker(name = "fallbackListaAfinidade", fallbackMethod = "listaAfinidadeDefault")
    @Cacheable("afinidades")
    public List<Afinidade> listar() throws Exception {
        log.info("---------------------------");
        log.info("Listando Afinidades");
        //TODO simular utilizacao do cache
        /*Thread.sleep(2000);*/
        List<Afinidade> listaAfinidade = afinidadeRepository.findAll();
        log.info("Afinidades: {}", listaAfinidade);
        return listaAfinidade;
    }

    public List<Afinidade> listaAfinidadeDefault(Exception e) throws Exception {
        log.info("Assumindo resposta de fallback para lista de afindidades");
        Gson gson = new Gson();
        Path arquivoJson = Path.of(diretorioJsonFallback);
        return gson.fromJson(Files.readString(arquivoJson), new TypeToken<List<Afinidade>>() {
        }.getType());
    }


}
