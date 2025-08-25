package com.example.model.dto;

import java.util.List;

public class TelemetricaPorData {

    private String dataReferencia;

    private List<TelemetricaDto> listaEndpoints;

    public TelemetricaPorData(String dataReferencia, List<TelemetricaDto> listaEndpoints) {
        this.dataReferencia = dataReferencia;
        this.listaEndpoints = listaEndpoints;
    }
}
