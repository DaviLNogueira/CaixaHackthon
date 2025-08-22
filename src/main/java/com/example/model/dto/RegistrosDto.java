package com.example.model.dto;

import com.example.model.domain.local.Simulacao;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrosDto {

    private Long idSimulacao ;
    private double valorDesejado;
    private int prazo;
    public RegistrosDto(Simulacao simulacao) {
       this.idSimulacao = simulacao.getIdSimulacao();
       this.valorDesejado = simulacao.getValorDesejado();
       this.prazo = simulacao.getPrazo();
    }
}
