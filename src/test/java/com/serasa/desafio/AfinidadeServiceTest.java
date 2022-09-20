package com.serasa.desafio;

import com.google.gson.reflect.TypeToken;
import com.serasa.desafio.infrastructure.repository.AfinidadeRepository;
import com.serasa.desafio.models.domain.Afinidade;
import com.serasa.desafio.models.domain.Pessoa;
import com.serasa.desafio.models.dto.request.CadastroAfinidadeRequestDTO;
import com.serasa.desafio.models.dto.response.PessoaResponseDTO;
import com.serasa.desafio.models.exception.CustomException;
import com.serasa.desafio.service.impl.AfinidadeServiceImpl;
import com.serasa.desafio.util.GeradorDeEntidades;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AfinidadeServiceTest {

    @InjectMocks
    private AfinidadeServiceImpl afinidadeService;
    @Mock
    private AfinidadeRepository afinidadeRepository;
    @Mock
    private ModelMapper modelMapper;
    private CadastroAfinidadeRequestDTO cadastroAfinidadeRequestDTO;
    private List<PessoaResponseDTO> pessoaResponseDTOLista;
    private List<Afinidade> afinidadeLista;
    private Pessoa pessoa;

    @BeforeEach
    public void criaEntidades() throws IOException {
        this.cadastroAfinidadeRequestDTO = (CadastroAfinidadeRequestDTO) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("cadastroAfinidadeDTO", CadastroAfinidadeRequestDTO.class);
        this.pessoaResponseDTOLista = (List<PessoaResponseDTO>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("pessoaResponseDTOLista", new TypeToken<List<PessoaResponseDTO>>() {
        });
        this.afinidadeLista = (List<Afinidade>) GeradorDeEntidades.gerarObjetoPeloNomeDoJsonLista("afinidadeLista", new TypeToken<List<Afinidade>>() {
        });
        this.pessoa = (Pessoa) GeradorDeEntidades.gerarObjetoPeloNomeDoJson("pessoa", Pessoa.class);

        ReflectionTestUtils.setField(afinidadeService, "diretorioJsonFallback", "src/test/resources/afinidadeListaFallback.json");
    }

    @Test
    public void testeSalvar() throws Exception {

        when(afinidadeRepository.findAll()).thenReturn(afinidadeLista);
        when(afinidadeRepository.findByRegiaoIgnoreCase(cadastroAfinidadeRequestDTO.getRegiao())).thenReturn(Optional.empty());
        when(modelMapper.map(cadastroAfinidadeRequestDTO, Afinidade.class)).thenReturn(afinidadeLista.get(0));
        List<Afinidade> listaAfinidade = afinidadeService.salvar(cadastroAfinidadeRequestDTO);
        Assertions.assertEquals(listaAfinidade.get(0).getEstados(), afinidadeLista.get(0).getEstados());
        Assertions.assertEquals(listaAfinidade.get(0).getRegiao(), afinidadeLista.get(0).getRegiao());
        Assertions.assertEquals(listaAfinidade.get(0).getId(), afinidadeLista.get(0).getId());

    }

    @Test
    public void testeMetodoDeFallBack() throws Exception {

        List<Afinidade> afinidadeListaResponse = afinidadeService.listaAfinidadeDefault(new JpaSystemException(new RuntimeException()));
        Assertions.assertEquals(afinidadeListaResponse, afinidadeLista);
    }


    @Test
    public void testeSalvarAfinidadeJaExistente() {

        when(afinidadeRepository.findByRegiaoIgnoreCase(cadastroAfinidadeRequestDTO.getRegiao())).thenReturn(Optional.of(afinidadeLista.get(0)));

        Assertions.assertThrows(CustomException.class, () -> {
            afinidadeService.salvar(cadastroAfinidadeRequestDTO);
        });
    }

    @Test
    public void testeFiltrarEstadosPorRegiaoSudeste() throws Exception {

        Afinidade estadosLista = afinidadeService.filtrarEstadosPorRegiao("sudeste", afinidadeLista);
        Assertions.assertEquals(estadosLista, afinidadeLista.get(0));

    }

    @Test
    public void testeFiltrarEstadosPorRegiaoSul() throws Exception {

        Afinidade estadosLista = afinidadeService.filtrarEstadosPorRegiao("sul", afinidadeLista);
        Assertions.assertEquals(estadosLista, afinidadeLista.get(1));

    }

    @Test
    public void testeFiltrarEstadosPorRegiaoComListVazia() throws Exception {

        Assertions.assertThrows(CustomException.class, () -> {
            afinidadeService.filtrarEstadosPorRegiao("sudeste", new ArrayList<>());
        });

    }

    @Test
    public void testeFiltrarEstadosPorRegiaoComRegiaoNaoEncontrada() throws Exception {

        Assertions.assertThrows(CustomException.class, () -> {
            afinidadeService.filtrarEstadosPorRegiao("regiaoquenaoexiste", afinidadeLista);
        });

    }

}
