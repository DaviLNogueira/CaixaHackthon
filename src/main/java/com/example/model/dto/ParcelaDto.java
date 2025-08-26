package com.example.model.dto;

import com.example.model.domain.local.Parcela;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ParcelaDto {  // REMOVE extends PanacheEntityBase

    private int numero;
    private double valorAmortizado;
    private double valorJuros;
    private double valorPrestacao;

    public ParcelaDto(Parcela parcela) {
        this.numero = parcela.getNumero();
        this.valorAmortizado = parcela.getValorAmortizado();
        this.valorJuros = parcela.getValorJuros();
        this.valorPrestacao = parcela.getValorPrestacao();
    }

}