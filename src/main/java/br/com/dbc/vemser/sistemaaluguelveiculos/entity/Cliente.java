package br.com.dbc.vemser.sistemaaluguelveiculos.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cliente {
    private Integer idCliente;
    private String nome;
    private String cpf;
}
