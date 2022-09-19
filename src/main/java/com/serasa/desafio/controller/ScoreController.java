package com.serasa.desafio.controller;

import com.serasa.desafio.models.dto.request.CadastroScoreRequestDTO;
import com.serasa.desafio.service.ScoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @ApiOperation(value = "Serviço responsável por cadastrar Scores")
    @PostMapping(path = "/score")
    @ResponseStatus(value = HttpStatus.CREATED)
    private void salvar(@Valid @RequestBody CadastroScoreRequestDTO cadastroScoreRequestDTO) throws Exception {
        scoreService.salvar(cadastroScoreRequestDTO);
    }


}
