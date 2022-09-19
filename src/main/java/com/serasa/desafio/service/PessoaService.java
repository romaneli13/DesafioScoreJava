package com.serasa.desafio.service;

import com.serasa.desafio.models.dto.request.CadastroPessoaRequestDTO;
import com.serasa.desafio.models.dto.response.PessoaResponseDTO;
import com.serasa.desafio.models.exception.CustomException;

import java.util.List;
import java.util.Optional;

public interface PessoaService {
    void salvar(CadastroPessoaRequestDTO cadastroPessoaRequestDTO) throws Exception;
    Optional<PessoaResponseDTO> buscar(Long idPessoa) throws Exception;
    Optional<List<PessoaResponseDTO>> listar() throws Exception;
}
