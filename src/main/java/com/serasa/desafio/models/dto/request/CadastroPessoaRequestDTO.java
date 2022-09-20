package com.serasa.desafio.models.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CadastroPessoaRequestDTO {

    @ApiModelProperty(value = "Nome da pessoa", example = "Fulano de tal")
    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @ApiModelProperty(value = "Telefone da pessoa", example = "1199999-9999")
    @NotBlank(message = "telefone é obrigatório")
    private String telefone;

    @ApiModelProperty(value = "Idade da pessoa", example = "18")
    @Min(message = "Idade minima permitida", value = 0)
    private Integer idade;

    @ApiModelProperty(value = "Cidade da pessoa", example = "Cidade de Fulano")
    @NotBlank(message = "Cidade é obrigatório")
    private String cidade;

    @ApiModelProperty(value = "Estado da pessoa", example = "SP")
    @NotBlank(message = "estado é obrigatório")
    private String estado;

    @ApiModelProperty(value = "Score da pessoa", example = "545")
    @Max(message = "Minimo 0 - Maximo 1000", value = 1000)
    private Integer score;

    @ApiModelProperty(value = "Regiao da pessoa", example = "sudeste")
    @NotBlank(message = "regiao é obrigatório")
    private String regiao;

}
