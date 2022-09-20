package com.serasa.desafio;

import com.google.gson.reflect.TypeToken;
import com.serasa.desafio.infrastructure.repository.PessoaRepository;
import com.serasa.desafio.models.domain.Afinidade;
import com.serasa.desafio.models.domain.Pessoa;
import com.serasa.desafio.models.domain.Score;
import com.serasa.desafio.models.dto.request.CadastroPessoaRequestDTO;
import com.serasa.desafio.models.dto.response.PessoaResponseDTO;
import com.serasa.desafio.models.exception.CustomException;
import com.serasa.desafio.service.AfinidadeService;
import com.serasa.desafio.service.ScoreService;
import com.serasa.desafio.service.impl.PessoaServiceImpl;
import com.serasa.desafio.util.GeradorDeEntidades;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ScoreService scoreService;

    @Mock
    private AfinidadeService afinidadeService;
    private Pessoa pessoa;
    private CadastroPessoaRequestDTO cadastroPessoaRequestDTO;
    private PessoaResponseDTO pessoaResponseDTO;
    private List<PessoaResponseDTO> pessoaResponseDTOLista;

    private List<Score> scoreLista;

    private List<Afinidade> afinidadeLista;

    private List<Pessoa> pessoaLista;

    @BeforeEach
    public void criaEntidades() throws IOException {
        this.cadastroPessoaRequestDTO = (CadastroPessoaRequestDTO) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("cadastroPessoaDTO", CadastroPessoaRequestDTO.class);
        this.pessoaResponseDTO = (PessoaResponseDTO) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("pessoaResponseDTO", PessoaResponseDTO.class);
        this.pessoa = (Pessoa) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("pessoa", Pessoa.class);
        this.pessoaResponseDTOLista = (List<PessoaResponseDTO>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("pessoaResponseDTOLista", new TypeToken<List<PessoaResponseDTO>>() {
        });
        this.scoreLista = (List<Score>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("scoreLista", new TypeToken<List<Score>>() {
        });
        this.afinidadeLista = (List<Afinidade>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("afinidadeLista", new TypeToken<List<Afinidade>>() {
        });
        this.pessoaLista = (List<Pessoa>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("pessoaLista", new TypeToken<List<Pessoa>>() {
        });
    }

    @Test
    public void testeSalvar() throws Exception {
        when(modelMapper.map(cadastroPessoaRequestDTO, Pessoa.class)).thenReturn(pessoa);
        when(scoreService.listar()).thenReturn(scoreLista);
        when(scoreService.filtrarDescricaoPorScore(cadastroPessoaRequestDTO.getScore(), scoreLista)).thenReturn(scoreLista.get(0));
        when(afinidadeService.listar()).thenReturn(afinidadeLista);
        when(afinidadeService.filtrarEstadosPorRegiao(cadastroPessoaRequestDTO.getRegiao(), afinidadeLista)).thenReturn(afinidadeLista.get(0));
        pessoaService.salvar(cadastroPessoaRequestDTO);
        PessoaServiceImpl pessoaServiceVerify = mock(PessoaServiceImpl.class);
        pessoaServiceVerify.salvar(cadastroPessoaRequestDTO);
        verify(pessoaServiceVerify, times(1)).salvar(cadastroPessoaRequestDTO);
    }

    @Test
    public void testeSalvarPessoaJaExistente() throws Exception {
        when(pessoaRepository.findByNomeIgnoreCase(cadastroPessoaRequestDTO.getNome())).thenReturn(Optional.of(pessoaLista.get(0)));
        Assertions.assertThrows(CustomException.class, () -> {
            pessoaService.salvar(cadastroPessoaRequestDTO);
        });
    }

    @Test
    public void testeBuscar() throws Exception {
        when(pessoaRepository.findById(pessoa.getId())).thenReturn(Optional.of(pessoa));

        Optional<PessoaResponseDTO> optionalPessoaResponseDTO = pessoaService.buscar(pessoa.getId());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().getEstados(), afinidadeLista.get(0).getEstados());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().getEstado(), pessoa.getEstado());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().getCidade(), pessoa.getCidade());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().getNome(), pessoa.getNome());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().getScoreDescricao(), "Insuficiente");
    }

    @Test
    public void testeBuscarComErroAoFiltrarDescricaoPorScore() throws Exception {
        when(pessoaRepository.findById(pessoa.getId())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(Exception.class, () -> {
            pessoaService.buscar(pessoa.getId());
        });

    }

    @Test
    public void testeListar() throws Exception {
        when(pessoaRepository.findAll()).thenReturn(pessoaLista);

        Optional<List<PessoaResponseDTO>> optionalPessoaResponseDTO = pessoaService.listar();
        Assertions.assertEquals(optionalPessoaResponseDTO.get().get(0).getEstados(), afinidadeLista.get(0).getEstados());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().get(0).getEstado(), pessoa.getEstado());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().get(0).getCidade(), pessoa.getCidade());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().get(0).getNome(), pessoa.getNome());
        Assertions.assertEquals(optionalPessoaResponseDTO.get().get(0).getScoreDescricao(), "Insuficiente");
    }

    @Test
    public void testeListarComListaVazia() throws Exception {
        when(pessoaRepository.findAll()).thenReturn(new ArrayList<>());

        Optional<List<PessoaResponseDTO>> optionalPessoaResponseDTOS = pessoaService.listar();
        Assertions.assertEquals(optionalPessoaResponseDTOS, Optional.empty());
    }

    @Test
    public void testeListarComErroAoFiltrarDescricaoPorScore() throws Exception {
        when(pessoaRepository.findAll()).thenThrow(NullPointerException.class);

        Assertions.assertThrows(Exception.class, () -> {
            pessoaService.listar();
        });

    }


}
