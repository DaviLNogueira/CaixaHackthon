package com.example.model.dto;

import com.example.model.domain.remoto.Produto;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EstatisticaDto extends PanacheEntityBase {

    private int codigoProduto;
    private String descricaoProduto;
    private double taxaMediaJuro;
    private double valorTotalCredito;
    private double valorTotalDesejado;
    private double valorMedioPrestacao;

    public EstatisticaDto(Produto produto, double taxaMediaJuro, double valorTotalCredito, double valorTotalDesejado, double valorMedioPrestacao) {
        this.codigoProduto = produto.getId();
        this.descricaoProduto = produto.getNome();
        this.taxaMediaJuro = taxaMediaJuro;
        this.valorTotalCredito = arrendodarValor(valorTotalCredito);
        this.valorTotalDesejado = arrendodarValor(valorTotalDesejado);
        this.valorMedioPrestacao = arrendodarValor(valorMedioPrestacao);
    }

    private double arrendodarValor(double valor){
        return Double.parseDouble(String.format("%.2f",valor ).replace
                (",", "."));
    }
}

