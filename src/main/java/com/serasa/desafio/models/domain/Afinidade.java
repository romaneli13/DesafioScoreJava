package com.serasa.desafio.models.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Data
@Entity
public class Afinidade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String regiao;

    @ElementCollection
    private List<String> estados;

    public static Optional<Afinidade> filtrarEstados(String regiao, List<Afinidade> listaAfinidades) {
        return listaAfinidades.stream().filter(afinidade -> afinidade.getRegiao().equalsIgnoreCase(regiao)).findAny();
    }

}
