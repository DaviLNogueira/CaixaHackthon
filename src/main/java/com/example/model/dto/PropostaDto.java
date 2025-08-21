package com.example.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PropostaDto {

    // Getters e Setters (obrigatórios para serialização/deserialização JSON)
    private Double valorDesejado;

    private int prazo;

    // Construtor padrão necessário para JAX-RS
    public PropostaDto() {}

    // Construtor com parâmetros
    public PropostaDto(Double valorDesejado, int prazo) {
        this.valorDesejado = valorDesejado;
        this.prazo = prazo;
    }

}