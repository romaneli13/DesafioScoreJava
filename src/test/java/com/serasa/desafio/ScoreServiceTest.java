package com.serasa.desafio;

import com.google.gson.reflect.TypeToken;
import com.serasa.desafio.infrastructure.repository.ScoreRepository;
import com.serasa.desafio.models.domain.Afinidade;
import com.serasa.desafio.models.domain.Pessoa;
import com.serasa.desafio.models.domain.Score;
import com.serasa.desafio.models.dto.request.CadastroScoreRequestDTO;
import com.serasa.desafio.models.dto.response.PessoaResponseDTO;
import com.serasa.desafio.models.exception.CustomException;
import com.serasa.desafio.service.AfinidadeService;
import com.serasa.desafio.service.impl.ScoreServiceImpl;
import com.serasa.desafio.util.GeradorDeEntidades;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {

    @InjectMocks
    private ScoreServiceImpl scoreService;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AfinidadeService afinidadeService;
    private Pessoa pessoa;
    private CadastroScoreRequestDTO cadastroScoreRequestDTO;
    private PessoaResponseDTO pessoaResponseDTO;
    private List<PessoaResponseDTO> pessoaResponseDTOLista;

    private List<Score> scoreLista;

    @BeforeEach
    public void criaEntidades() throws IOException {
        this.cadastroScoreRequestDTO = (CadastroScoreRequestDTO) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("cadastroScoreDTO", CadastroScoreRequestDTO.class);
        this.pessoaResponseDTO = (PessoaResponseDTO) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("pessoaResponseDTO", PessoaResponseDTO.class);
        this.pessoa = (Pessoa) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("pessoa", Pessoa.class);
        this.pessoaResponseDTOLista = (List<PessoaResponseDTO>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("pessoaResponseDTOLista", new TypeToken<List<PessoaResponseDTO>>() {
        });
        this.scoreLista = (List<Score>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("scoreLista", new TypeToken<List<Score>>() {
        });
        ReflectionTestUtils.setField(scoreService, "diretorioJsonFallback", "src/test/resources/scoreListaFallback.json");
    }

    @Test
    public void testeSalvarScoreInsuficiente() throws Exception {
        when(scoreRepository.findByDescricaoIgnoreCase("Insuficiente")).thenReturn(Optional.empty());
        when(modelMapper.map(cadastroScoreRequestDTO, Score.class)).thenReturn(scoreLista.get(0));
        when(scoreRepository.findAll()).thenReturn(scoreLista);
        List<Score> scoreLista = scoreService.salvar(cadastroScoreRequestDTO);
        Assertions.assertEquals(scoreLista.get(0).getScoreFinal(), cadastroScoreRequestDTO.getScoreFinal());
        Assertions.assertEquals(scoreLista.get(0).getScoreInicial(), cadastroScoreRequestDTO.getScoreInicial());
        Assertions.assertEquals(scoreLista.get(0).getDescricao(), cadastroScoreRequestDTO.getScoreDescricao());
    }


    @Test
    public void testeSalvarComErroDeScoreJaCadastrado() {
        when(scoreRepository.findByDescricaoIgnoreCase("Insuficiente")).thenReturn(Optional.of(scoreLista.get(0)));
        Assertions.assertThrows(CustomException.class, () -> {
            scoreService.salvar(cadastroScoreRequestDTO);
        });
    }

    @Test
    public void testFiltrarDescricaoPorScoreRecomendavel() throws Exception {
        String scoreDescricaoRecomendavel = scoreService.filtrarDescricaoPorScore(1000, scoreLista);
        Assertions.assertEquals(scoreDescricaoRecomendavel, "Recomendável");
    }

    @Test
    public void testFiltrarDescricaoPorScoreInsuficiente() throws Exception {
        String scoreDescricaoInsuficiente = scoreService.filtrarDescricaoPorScore(0, scoreLista);
        Assertions.assertEquals(scoreDescricaoInsuficiente, "Insuficiente");
    }

    @Test
    public void testFiltrarDescricaoPorScoreComDescricaoNaoEncontrada() throws CustomException {
        Assertions.assertThrows(CustomException.class, () -> {
            scoreService.filtrarDescricaoPorScore(1001, scoreLista);
        });
    }

    @Test
    public void testFiltrarDescricaoPorScoreInaceitavel() throws Exception {
        String scoreDescricaoInaceitavel = scoreService.filtrarDescricaoPorScore(206, scoreLista);
        Assertions.assertEquals(scoreDescricaoInaceitavel, "Inaceitável");
    }

    @Test
    public void testFiltrarDescricaoPorScoreAceitavel() throws Exception {
        String scoreDescricaoAceitavel = scoreService.filtrarDescricaoPorScore(501, scoreLista);
        Assertions.assertEquals(scoreDescricaoAceitavel, "Aceitável");
    }

    @Test
    public void testListar() throws Exception {
        when(scoreRepository.findAll()).thenReturn(scoreLista);
        List<Score> scoreDescricaoAceitavel = scoreService.listar();
        Assertions.assertEquals(scoreDescricaoAceitavel, scoreLista);
    }

    @Test
    public void testeMetodoDeFallBack() throws Exception {

        List<Score> scoreListaResponse = scoreService.listaScoreDefault(new JpaSystemException(new RuntimeException()));
        Assertions.assertEquals(scoreListaResponse, scoreLista);
    }


}
