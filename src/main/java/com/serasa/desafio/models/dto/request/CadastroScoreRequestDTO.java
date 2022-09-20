package com.serasa.desafio.models.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jdk.jfr.MemoryAddress;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class CadastroScoreRequestDTO {

    @ApiModelProperty(value = "Descrição do Score", example = "Insuficiente")
    private String scoreDescricao;

    @ApiModelProperty(value = "Score inicial", example = "0")
    @JsonProperty(value = "inicial")
    private Integer scoreInicial;

    @ApiModelProperty(value = "Score Final", example = "500")
    @JsonProperty(value = "final")
    @Max(value = 1000, message = "Valor maximo do score é 1000")
    private Integer scoreFinal;


}
