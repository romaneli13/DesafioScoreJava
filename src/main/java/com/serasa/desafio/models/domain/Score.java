package com.serasa.desafio.models.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Data
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String descricao;
    @Column(name = "inicial")
    private Integer scoreInicial;
    @Column(name = "final")
    private Integer scoreFinal;


    public static Optional<Score> filtrarDescricao(Integer scorePessoa, List<Score> listaScore) {
        return listaScore.stream().filter(score -> (scorePessoa >= score.getScoreInicial()) && scorePessoa <= score.getScoreFinal()).findAny();
    }

}
