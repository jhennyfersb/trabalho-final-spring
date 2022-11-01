package br.com.dbc.vemser.sistemaaluguelveiculos.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "LocacaoEntity")
public class LocacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_LOCACAO")
    @SequenceGenerator(name = "SEQ_LOCACAO",sequenceName = "seq_locacao",allocationSize = 1)
    @Column(name = "id_locacao")
    private Integer idLocacao;
    @Column(name = "data_locacao")
    private LocalDate dataLocacao;
    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "valor_locacao_total")
    private Double valorLocacao;

//    private ClienteEntity clienteEntity;
//    private VeiculoEntity veiculoEntity;
//    private CartaoCreditoEntity cartaoCreditoEntity;
//    private FuncionarioEntity funcionarioEntity;

//    public LocacaoEntity(Integer idLocacao,
//                         LocalDate dataLocacao,
//                         LocalDate dataDevolucao,
//                         ClienteEntity clienteEntity,
//                         VeiculoEntity veiculo,
//                         CartaoCreditoEntity cartaoCreditoEntity,
//                         FuncionarioEntity funcionarioEntity) {
//        this.idLocacao = idLocacao;
//        this.dataLocacao = dataLocacao;
//        this.dataDevolucao = dataDevolucao;
//        this.clienteEntity = clienteEntity;
//        this.veiculoEntity = veiculo;
//        this.cartaoCreditoEntity = cartaoCreditoEntity;
//        this.funcionarioEntity = funcionarioEntity;
//    }
}