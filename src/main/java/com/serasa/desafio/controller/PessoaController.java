package com.serasa.desafio.controller;

import com.serasa.desafio.models.dto.request.CadastroPessoaRequestDTO;
import com.serasa.desafio.models.dto.response.PessoaResponseDTO;
import com.serasa.desafio.models.exception.CustomException;
import com.serasa.desafio.service.PessoaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @ApiOperation(value = "Serviço responsável por cadastrar Pessoas")
    @PostMapping(path = "/pessoa")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Salvo com sucesso"),
            @ApiResponse(code = 409, message = "Dado inserido já existe"),
    })
    private ResponseEntity<Void> salvar(@Valid @RequestBody CadastroPessoaRequestDTO cadastroPessoaRequestDTO) throws Exception {
        pessoaService.salvar(cadastroPessoaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Serviço responsável por buscar Pessoa por ID")
    @GetMapping(path = "/pessoa/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca feita com sucesso"),
            @ApiResponse(code = 204, message = "Não existe registro para ID informado"),
    })
    private ResponseEntity<PessoaResponseDTO> buscar(@PathVariable("id") Long idPessoa) throws Exception {
        Optional<PessoaResponseDTO> optionalPessoaResponseDTO = pessoaService.buscar(idPessoa);
        return optionalPessoaResponseDTO.map(pessoaResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(pessoaResponseDTO)).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @ApiOperation(value = "Serviço responsável por listar pessoas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Listagem feita com sucesso"),
            @ApiResponse(code = 204, message = "Não existem registros"),
    })
    @GetMapping(path = "/pessoa")
    private ResponseEntity<List<PessoaResponseDTO>> listar() throws Exception {
        Optional<List<PessoaResponseDTO>> optionalPessoaResponseDTO = pessoaService.listar();
        return optionalPessoaResponseDTO.map(pessoaResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(pessoaResponseDTO)).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

}
