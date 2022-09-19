package com.serasa.desafio.service.impl;

import com.google.gson.Gson;
import com.serasa.desafio.infrastructure.repository.PessoaRepository;
import com.serasa.desafio.models.domain.Afinidade;
import com.serasa.desafio.models.domain.Pessoa;
import com.serasa.desafio.models.domain.Score;
import com.serasa.desafio.models.dto.request.CadastroPessoaRequestDTO;
import com.serasa.desafio.models.dto.response.PessoaResponseDTO;
import com.serasa.desafio.models.exception.CustomException;
import com.serasa.desafio.service.AfinidadeService;
import com.serasa.desafio.service.PessoaService;
import com.serasa.desafio.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PessoaServiceImpl implements PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AfinidadeService afinidadeService;

    @Autowired
    private ScoreService scoreService;

    private final Gson gson = new Gson();

    public void salvar(CadastroPessoaRequestDTO cadastroPessoaRequestDTO) throws Exception {
        log.info("---------------------------");
        log.info("Cadastrar Pessoa: {}", cadastroPessoaRequestDTO);
        Optional<Pessoa> optionalPessoa = pessoaRepository.findByNomeIgnoreCase(cadastroPessoaRequestDTO.getNome());
        if (optionalPessoa.isPresent()) {
            log.error("Cadastro de Pessoa já existe para o nome: {}", cadastroPessoaRequestDTO.getNome());
            throw new CustomException(HttpStatus.CONFLICT, "Cadastro de Pessoa já existe.");
        }
        Pessoa pessoa = modelMapper.map(cadastroPessoaRequestDTO, Pessoa.class);
        pessoaRepository.save(pessoa);
        log.info("Cadastro Criado");
    }

    public Optional<PessoaResponseDTO> buscar(Long idPessoa) throws Exception {
        log.info("---------------------------");
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(idPessoa);
        log.info("Buscar Pessoa, id: {}", idPessoa);
        return pessoaOptional.map(pessoa ->
        {
            try {
                return PessoaResponseDTO.builder()
                        .nome(pessoa.getNome())
                        .estado(pessoa.getEstado())
                        .cidade(pessoa.getCidade())
                        .scoreDescricao(scoreService.filtrarDescricaoPorScore(pessoaOptional.get().getScore(), scoreService.listar()))
                        .estados(afinidadeService.filtrarEstadosPorRegiao(pessoa.getRegiao(), afinidadeService.listar()))
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<List<PessoaResponseDTO>> listar() throws Exception {
        log.info("---------------------------");
        List<Pessoa> optionalPessoas = pessoaRepository.findAll();
        log.info("Listar Pessoas");
        if (optionalPessoas.isEmpty()) {
            log.info("Lista de pessoas vazia");
            return Optional.empty();
        } else {
            List<PessoaResponseDTO> pessoaResponseDTOLista = new ArrayList<>();
            List<Score> scoreLista = scoreService.listar();
            List<Afinidade> afinidadeLista = afinidadeService.listar();
            optionalPessoas.forEach(pessoa -> {
                try {
                    pessoaResponseDTOLista.add(PessoaResponseDTO.builder()
                            .nome(pessoa.getNome())
                            .estado(pessoa.getEstado())
                            .cidade(pessoa.getCidade())
                            .scoreDescricao(scoreService.filtrarDescricaoPorScore(pessoa.getScore(), scoreLista))
                            .estados(afinidadeService.filtrarEstadosPorRegiao(pessoa.getRegiao(), afinidadeLista))
                            .build());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return Optional.of(pessoaResponseDTOLista);
        }
    }
}
