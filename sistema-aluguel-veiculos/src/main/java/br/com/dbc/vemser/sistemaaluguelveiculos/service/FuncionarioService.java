package br.com.dbc.vemser.sistemaaluguelveiculos.service;
import br.com.dbc.vemser.sistemaaluguelveiculos.dto.FuncionarioCreateDTO;
import br.com.dbc.vemser.sistemaaluguelveiculos.dto.FuncionarioDTO;
import br.com.dbc.vemser.sistemaaluguelveiculos.exceptions.BancoDeDadosException;
import br.com.dbc.vemser.sistemaaluguelveiculos.entity.Funcionario;
import br.com.dbc.vemser.sistemaaluguelveiculos.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.sistemaaluguelveiculos.repository.FuncionarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final ObjectMapper objectMapper;

    // criação de um objeto
    public FuncionarioDTO create(FuncionarioCreateDTO funcionario) throws BancoDeDadosException {
//        try {
            Funcionario funcionarioAdicionado = funcionarioRepository.create(converterEmFuncionario(funcionario));
            return converterEmDTO(funcionarioAdicionado);
//        } catch (BancoDeDadosException e) {
//            e.printStackTrace();
//        }
    }

    // remoção
    public void delete(Integer id) throws BancoDeDadosException {
//        try {
            funcionarioRepository.delete(id);
//        } catch (BancoDeDadosException e) {
//            e.printStackTrace();
//        }
    }

    // atualização de um objeto
    public FuncionarioDTO update(Integer id, FuncionarioCreateDTO funcionario) throws BancoDeDadosException {
//        try {
            return objectMapper.convertValue(funcionarioRepository.update(id, converterEmFuncionario(funcionario)), FuncionarioDTO.class);
//        } catch (BancoDeDadosException e) {
//            e.printStackTrace();
//        }
    }

    // leitura
    public List<FuncionarioDTO> list() throws BancoDeDadosException {
//        try {
            List<Funcionario> listar = funcionarioRepository.list();
            return listar
                    .stream()
                    .map(this::converterEmDTO)
                    .collect(Collectors.toList());
//        } catch (BancoDeDadosException e) {
//            e.printStackTrace();
//        }
    }

    public Funcionario converterEmFuncionario(FuncionarioCreateDTO funcionarioCreateDTO){
        return objectMapper.convertValue(funcionarioCreateDTO, Funcionario.class);
    }

    public FuncionarioDTO converterEmDTO(Funcionario funcionario){
        return objectMapper.convertValue(funcionario, FuncionarioDTO.class);
    }

     Funcionario findById(Integer id) throws RegraDeNegocioException, BancoDeDadosException {
        return funcionarioRepository.list()
                .stream()
                .filter(funcionario -> funcionario.getIdFuncionario().equals(id))
                .findFirst()
                .orElseThrow(()-> new RegraDeNegocioException("Funcionario não encontrado"));
    }
}

