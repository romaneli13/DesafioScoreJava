package com.serasa.desafio.service;

import com.serasa.desafio.models.domain.Afinidade;
import com.serasa.desafio.models.dto.request.CadastroAfinidadeRequestDTO;
import com.serasa.desafio.models.exception.CustomException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface AfinidadeService {
    List<Afinidade> salvar(CadastroAfinidadeRequestDTO cadastroAfinidadeRequestDTO) throws Exception;
    List<Afinidade> listar() throws Exception;
    Afinidade filtrarEstadosPorRegiao(String regiao, List<Afinidade> listaAfinidades) throws Exception;


}
