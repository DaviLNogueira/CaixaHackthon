package com.example.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TelemetricaPorData {

    private String dataReferencia;

    private List<TelemetricaDto> listaEndpoints;

    public TelemetricaPorData(Date dataReferencia, List<TelemetricaDto> listaEndpoints) {
        this.dataReferencia = (new SimpleDateFormat("yyyy-MM-dd")).format(dataReferencia);
        this.listaEndpoints = listaEndpoints;
    }
}
