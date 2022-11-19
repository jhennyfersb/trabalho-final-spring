package br.com.dbc.vemser.sistemaaluguelveiculos.service;

import br.com.dbc.vemser.sistemaaluguelveiculos.dto.PageDTO;
import br.com.dbc.vemser.sistemaaluguelveiculos.dto.VeiculoCreateDTO;
import br.com.dbc.vemser.sistemaaluguelveiculos.dto.VeiculoDTO;
import br.com.dbc.vemser.sistemaaluguelveiculos.entity.VeiculoEntity;
import br.com.dbc.vemser.sistemaaluguelveiculos.entity.enums.DisponibilidadeVeiculo;
import br.com.dbc.vemser.sistemaaluguelveiculos.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.sistemaaluguelveiculos.repository.VeiculoRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VeiculoServiceTest {
    @InjectMocks // classe principal de testes
    private VeiculoService veiculoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private LogService logService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(veiculoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        VeiculoCreateDTO veiculoCreateDTO = getVeiculoCreateDTO();
        VeiculoEntity veiculoEntity = getVeiculoEntity();
        SecurityContextHolder.getContext().setAuthentication(getAuthentication());

        veiculoEntity.setIdVeiculo(10);
        when(veiculoRepository.save(any())).thenReturn(veiculoEntity);

        // Ação (ACT)
        VeiculoDTO veiculoDTO = veiculoService.create(veiculoCreateDTO);

        // Verificação (ASSERT)
        Assertions.assertNotNull(veiculoDTO);
        Assertions.assertNotNull(veiculoDTO.getIdVeiculo());
        Assertions.assertEquals("Honda", veiculoDTO.getMarca());
        verify(logService, times(1)).salvarLog(any());
    }

    @Test
    public void deveTestarListComSucesso() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        Integer pagina = 10;
        Integer quantidade = 5;
        VeiculoEntity veiculoEntity = getVeiculoEntity();
        Page<VeiculoEntity> paginaMock = new PageImpl<>(List.of(veiculoEntity));

        when(veiculoRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);

        SecurityContextHolder.getContext().setAuthentication(getAuthentication());

//        // ACT
        PageDTO<VeiculoDTO> paginaSolicitada = veiculoService.list(pagina, quantidade);

//        // ASSERT
        Assertions.assertNotNull(paginaSolicitada);
        Assertions.assertEquals(1, paginaSolicitada.getQuantidadePaginas());
        Assertions.assertEquals(1, paginaSolicitada.getTotalElementos());
        verify(logService, times(1)).salvarLog(any());
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        Integer busca = 10;
        SecurityContextHolder.getContext().setAuthentication(getAuthentication());
        VeiculoEntity veiculoEntity = getVeiculoEntity();
        veiculoEntity.setIdVeiculo(busca);

        when(veiculoRepository.findById(anyInt())).thenReturn(Optional.of(veiculoEntity));

        // Ação (ACT)
        VeiculoDTO veiculoDTO = veiculoService.findById(busca, false);

        // Verificação (ASSERT)
        Assertions.assertNotNull(veiculoDTO);
        Assertions.assertEquals(10, veiculoDTO.getIdVeiculo());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarFindByIdComErro() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        Integer busca = 10;
        when(veiculoRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Ação (ACT)
        veiculoService.findById(busca, false);
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        Integer id = 10;
        SecurityContextHolder.getContext().setAuthentication(getAuthentication());

        //pessoaRepository.findById(id)
        VeiculoEntity veiculoEntity = getVeiculoEntity();
        veiculoEntity.setIdVeiculo(10);
        when(veiculoRepository.findById(anyInt())).thenReturn(Optional.of(veiculoEntity));

        // Ação (ACT)
        veiculoService.delete(id);

        // Verificação (ASSERT)
        verify(veiculoRepository, times(1)).deleteById(any());
        verify(logService, times(1)).salvarLog(any());
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException {
        // SETUP
        Integer id= 10;
        VeiculoCreateDTO veiculoCreateDTO = getVeiculoCreateDTO();
        SecurityContextHolder.getContext().setAuthentication(getAuthentication());

        VeiculoEntity veiculoEntity = getVeiculoEntity();
        veiculoEntity.setModelo("Civic");
        veiculoEntity.setIdVeiculo(10);
        when(veiculoRepository.findById(anyInt())).thenReturn(Optional.of(veiculoEntity));

        VeiculoEntity veiculoEntity1 = getVeiculoEntity();
        when(veiculoRepository.save(any())).thenReturn(veiculoEntity1);

        // ACT
        VeiculoDTO veiculoDTO = veiculoService.update(id, veiculoCreateDTO);

        // ASSERT
        Assertions.assertNotNull(veiculoDTO);
        Assertions.assertNotEquals("Civic", veiculoDTO.getModelo());
        verify(logService, times(1)).salvarLog(any());
    }

    private static VeiculoEntity getVeiculoEntity() {
        VeiculoEntity veiculoEntity = new VeiculoEntity();
        veiculoEntity.setDisponibilidadeVeiculo(DisponibilidadeVeiculo.DISPONIVEL);
        veiculoEntity.setAno(2022);
        veiculoEntity.setCor("Preto");
        veiculoEntity.setMarca("Honda");
        veiculoEntity.setModelo("Fit");
        veiculoEntity.setPlaca("KLA3030");
        veiculoEntity.setQuilometragem(2500.0);
        veiculoEntity.setValorLocacao(220.0);
        return veiculoEntity;
    }

    private static VeiculoCreateDTO getVeiculoCreateDTO() {
        VeiculoCreateDTO veiculoCreateDTO = new VeiculoCreateDTO();
        veiculoCreateDTO.setDisponibilidadeVeiculo(DisponibilidadeVeiculo.DISPONIVEL);
        veiculoCreateDTO.setAno(2022);
        veiculoCreateDTO.setCor("Preto");
        veiculoCreateDTO.setMarca("Honda");
        veiculoCreateDTO.setModelo("Fit");
        veiculoCreateDTO.setPlaca("KLA3030");
        veiculoCreateDTO.setQuilometragem(2500.0);
        veiculoCreateDTO.setValorLocacao(220.0);
        return veiculoCreateDTO;
    }

    private static UsernamePasswordAuthenticationToken getAuthentication(){
        return new UsernamePasswordAuthenticationToken(1, null, Collections.emptyList());
    }
}
