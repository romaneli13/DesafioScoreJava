package com.serasa.desafio.controller;

import com.serasa.desafio.models.dto.request.CadastroAfinidadeRequestDTO;
import com.serasa.desafio.service.AfinidadeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AfinidadeController {

    @Autowired
    private AfinidadeService afinidadeService;

    @ApiOperation(value = "Serviço responsável por Cadastrar Afinidades")
    @PostMapping(path = "/afinidade")
    @ResponseStatus(value = HttpStatus.CREATED)
    private void salvar(@Valid @RequestBody CadastroAfinidadeRequestDTO cadastroAfinidadeRequestDTO) throws Exception {
        afinidadeService.salvar(cadastroAfinidadeRequestDTO);
    }

}
