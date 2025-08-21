package com.example.model.dto;

import com.example.model.domain.remoto.Produto;
import com.example.model.domain.local.TipoEmprestimo;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class RespostaPropostaDto extends PanacheEntityBase {

    private int idSimulacao;
    private int codigoProduto;
    private String descricaoProduto;
    private double taxaJuros;
    private List<TipoEmprestimo> resultadoSimulacao;


    public RespostaPropostaDto(Produto produto,List<TipoEmprestimo> resultadoSimulacao) {
        //TODO : salvar simulacao
        this.idSimulacao = 0;
        this.codigoProduto = produto.getId();
        this.descricaoProduto = produto.getNome();
        this.taxaJuros = produto.getTaxaJuros();
        this.resultadoSimulacao = resultadoSimulacao;

    }

}
