package com.example.model.dto;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EstatisticaDto extends PanacheEntityBase {

    private int codigoProduto;
    private double taxaMediaJuro;
    private double valorTotalCredito;
    private double valorTotalDesejado;
    private double valorMedioPrestacao;

    public EstatisticaDto(int codigoProduto, double taxaMediaJuro, double valorTotalCredito, double valorTotalDesejado, double valorMedioPrestacao) {
        this.codigoProduto = codigoProduto;
        this.taxaMediaJuro = taxaMediaJuro;
        this.valorTotalCredito = valorTotalCredito;
        this.valorTotalDesejado = valorTotalDesejado;
        this.valorMedioPrestacao = valorMedioPrestacao;
    }
}
