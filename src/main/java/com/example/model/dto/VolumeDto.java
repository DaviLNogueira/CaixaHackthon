package com.example.model.dto;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class VolumeDto extends PanacheEntityBase {

    private String dataReferencia;
    private List<EstatisticaDto> estatisticas;

    public VolumeDto(Date dataReferencia, List<EstatisticaDto> estatisticas) {
        this.dataReferencia = (new SimpleDateFormat("yyyy-MM-dd")).format(dataReferencia);
        this.estatisticas = estatisticas;
    }

}
