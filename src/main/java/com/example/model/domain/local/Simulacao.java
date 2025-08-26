package com.example.model.domain.local;

import com.example.model.domain.remoto.Produto;
import com.example.model.dto.PropostaDto;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "SIMULACAO")
@PersistenceUnit(name = "local")
public class Simulacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SIMULACAO")
    private Long idSimulacao;

    @Column(name = "CODIGO_PRODUTO")
    private int codigoProduto;

    @Column(name = "TAXA_JUROS")
    private double taxaJuros; // valor pode mudar

    @Column(name = "PRAZO")
    private int prazo;

    @Column(name = "VALOR_DESEJADO")
    private double valorDesejado;

    @Column(name = "VALOR_MEDIO_PRESTACAO")
    private double valorMedioPrestacao = 0;

    @Column(name = "VALOR_TOTAL_CREDITO")
    private double valorTotalCredito = 0;


    @Column(name = "DATA_REFERENCIA")
    private Date dataReferencia;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoEmprestimo> tipoEmprestimos = new ArrayList<>();


    private String descricaoProduto;

    public void addTipoEmprestimo(TipoEmprestimo tipoEmprestimo) {
        tipoEmprestimo.setSimulacao(this);
        this.tipoEmprestimos.add(tipoEmprestimo);
        selecionarMelhorTipoEmprestimo(tipoEmprestimo);
    }

    public void selecionarMelhorTipoEmprestimo(TipoEmprestimo tipoEmprestimo) {
        if(valorTotalCredito == 0){
            valorTotalCredito = tipoEmprestimo.getValorTotalCredito();
            valorMedioPrestacao = tipoEmprestimo.getValorMedioParcela();
        } else if (tipoEmprestimo.getValorTotalCredito() < valorTotalCredito) {
            valorTotalCredito = tipoEmprestimo.getValorTotalCredito();
            valorMedioPrestacao = tipoEmprestimo.getValorMedioParcela();

        }

    }

    public Simulacao(Produto produto, PropostaDto propostaDto) {
        this.codigoProduto = produto.getId();
        this.descricaoProduto = produto.getNome();
        this.taxaJuros = produto.getTaxaJuros();
        this.prazo = propostaDto.getPrazo();
        this.valorDesejado = propostaDto.getValorDesejado();
        this.dataReferencia = new Date();


    }

    public Simulacao() {

    }
}
