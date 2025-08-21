package com.example.model.dto;

import com.example.model.domain.local.Parcela;

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

    // Getters
    public int getNumero() { return numero; }
    public double getValorAmortizado() { return valorAmortizado; }
    public double getValorJuros() { return valorJuros; }
    public double getValorPrestacao() { return valorPrestacao; }
}