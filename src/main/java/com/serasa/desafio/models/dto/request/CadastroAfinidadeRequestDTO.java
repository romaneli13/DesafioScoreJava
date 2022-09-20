package com.serasa.desafio.models.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CadastroAfinidadeRequestDTO {

    @NotBlank(message = "Regiao é obrigatório")
    @ApiModelProperty(value = "Regiao", example = "sudeste")
    private String regiao;

    @NotEmpty(message = "Lista de estados é obrigatório")
    @ApiModelProperty(value = "Estados da pessoa", example = "[\"SP\"]")
    private List<String> estados;


}
