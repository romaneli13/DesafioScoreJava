package com.serasa.desafio.models.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CadastroScoreRequestDTO {

    @ApiModelProperty(value = "Descrição do Score", example = "Insuficiente")
    private String scoreDescricao;

    @ApiModelProperty(value = "Score inicial", example = "0")
    @JsonProperty(value = "inicial")
    private Integer scoreInicial;

    @ApiModelProperty(value = "Score Final", example = "500")
    @JsonProperty(value = "final")
    private Integer scoreFinal;


}
