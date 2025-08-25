package com.example.model.dto;

public class TelemetricaDto {

    private String nomeApi;
    private int qtdRequisicoes;
    private double tempoMedio;
    private double tempoMinimo;
    private double tempoMaximo;
    private double porcentalSucesso;

    public TelemetricaDto(String nomeApi,
                          int qtdRequisicoes,
                          double tempoMedio, double tempoMinimo,
                          double tempoMaximo,
                          double porcentalSucesso) {
        this.nomeApi = nomeApi;
        this.qtdRequisicoes = qtdRequisicoes;
        this.tempoMedio = tempoMedio;
        this.tempoMinimo = tempoMinimo;
        this.tempoMaximo = tempoMaximo;
        this.porcentalSucesso = porcentalSucesso;
    }
}