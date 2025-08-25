package com.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PropostaDto {

    @NotNull(message = "O campo valorDesejado é obrigatório")
    // Getters e Setters (obrigatórios para serialização/deserialização JSON)
    private Double valorDesejado;

    @NotNull(message = "O campo prazo é obrigatório")
    private int prazo;

    // Construtor padrão necessário para JAX-RS
    public PropostaDto() {}

    // Construtor com parâmetros
    public PropostaDto(Double valorDesejado, int prazo) {
        this.valorDesejado = valorDesejado;
        this.prazo = prazo;
    }

}