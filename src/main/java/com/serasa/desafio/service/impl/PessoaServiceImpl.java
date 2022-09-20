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
        pessoa.setScore(scoreService.filtrarDescricaoPorScore(cadastroPessoaRequestDTO.getScore(), scoreService.listar()));
        pessoa.setAfinidade(afinidadeService.filtrarEstadosPorRegiao(cadastroPessoaRequestDTO.getRegiao(), afinidadeService.listar()));
        Pessoa pessoaCadastrada = pessoaRepository.save(pessoa);
        log.info("Pessoa cadastrada com o id: {}", pessoaCadastrada);
        log.info("Cadastro Criado");
    }

    public Optional<PessoaResponseDTO> buscar(Long idPessoa) throws Exception {
        log.info("---------------------------");
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(idPessoa);
        log.info("Teste {}", gson.toJsonTree(pessoaOptional.get()));
        log.info("Buscar Pessoa, id: {}", idPessoa);
        return pessoaOptional.map(pessoa ->
                PessoaResponseDTO.builder()
                        .nome(pessoa.getNome())
                        .estado(pessoa.getEstado())
                        .cidade(pessoa.getCidade())
                        .scoreDescricao(pessoa.getScore().getDescricao())
                        .estados(pessoa.getAfinidade().getEstados())
                        .build());
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
            optionalPessoas.forEach(pessoa -> {
                log.info("Pessoa: {}", pessoa);
                pessoaResponseDTOLista.add(PessoaResponseDTO.builder()
                        .nome(pessoa.getNome())
                        .estado(pessoa.getEstado())
                        .cidade(pessoa.getCidade())
                        .scoreDescricao(pessoa.getScore().getDescricao())
                        .estados(pessoa.getAfinidade().getEstados())
                        .build());
            });
            return Optional.of(pessoaResponseDTOLista);
        }
    }
}
