package com.serasa.desafio.controller;

import com.serasa.desafio.models.dto.request.CadastroAfinidadeRequestDTO;
import com.serasa.desafio.service.AfinidadeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AfinidadeController {

    @Autowired
    private AfinidadeService afinidadeService;

    @ApiOperation(value = "Serviço responsável por Cadastrar Afinidades")
    @PostMapping(path = "/afinidade")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Salvo com sucesso"),
            @ApiResponse(code = 409, message = "Dado inserido já existe"),
    })
    private ResponseEntity<Void> salvar(@Valid @RequestBody CadastroAfinidadeRequestDTO cadastroAfinidadeRequestDTO) throws Exception {
        afinidadeService.salvar(cadastroAfinidadeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
