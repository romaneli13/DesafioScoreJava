package com.serasa.desafio.models.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PessoaResponseDTO {

    @ApiModelProperty(value = "Nome da pessoa")
    private String nome;

    @ApiModelProperty(value = "Cidade da pessoa")
    private String cidade;

    @ApiModelProperty(value = "Estado da pessoa")
    private String estado;

    @ApiModelProperty(value = "Descrição do Score da pessoa")
    private String scoreDescricao;

    @ApiModelProperty(value = "Estados de afinidade da pessoa")
    private List<String> estados;

}
