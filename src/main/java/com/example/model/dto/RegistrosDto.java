package com.example.model.dto;

import com.example.model.domain.local.Simulacao;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrosDto {

    private Long idSimulacao ;
    private double valorDesejado;
    private int prazo;
    private double valorTotalParcelas;
    public RegistrosDto(Simulacao simulacao) {
       this.idSimulacao = simulacao.getIdSimulacao();
       this.valorDesejado = arrendodarValor(simulacao.getValorDesejado());
       this.prazo = simulacao.getPrazo();
       this.valorTotalParcelas = arrendodarValor(simulacao.getValorTotalCredito());
    }

    private double arrendodarValor(double valor){
        return Double.parseDouble(String.format("%.2f",valor ).replace
                (",", "."));
    }
}
