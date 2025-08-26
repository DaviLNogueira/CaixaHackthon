package com.example.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelemetricaDto {

    private String nomeApi;
    private long qtdRequisicoes;
    private double tempoMedio;
    private double tempoMinimo;
    private double tempoMaximo;
    private double porcentualSucesso;

    public TelemetricaDto(String nomeApi,
                          long qtdRequisicoes,
                          double tempoMedio, double tempoMinimo,
                          double tempoMaximo,
                          double porcentalSucesso) {
        this.nomeApi = nomeApi;
        this.qtdRequisicoes = qtdRequisicoes;
        this.tempoMedio = arrendodarValor(tempoMedio);
        this.tempoMinimo = arrendodarValor(tempoMinimo);
        this.tempoMaximo = arrendodarValor(tempoMaximo);
        this.porcentualSucesso = arrendodarValor(porcentalSucesso);
    }

    private double arrendodarValor(double valor){
        return Double.parseDouble(String.format("%.2f",valor ).replace
                (",", "."));
    }

}