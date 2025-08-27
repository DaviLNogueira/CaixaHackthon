package com.example.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Setter
@Getter
@NotNull
public class PropostaDto {

    @Schema(defaultValue = "1000")
    @NotNull(message = "O campo valorDesejado é obrigatório")
    private Double valorDesejado;

    @Schema(defaultValue = "5")
    @NotNull(message = "O campo prazo é obrigatório")
    @Positive(message = "O campo prazo deve estar presente e maior zero")
    public int prazo;

    public PropostaDto() {}

}