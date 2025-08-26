package com.example.model.dto;

import com.example.model.domain.local.Simulacao;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class SimulacoesDto {

    private int pagina;
    private Long qtdRegistros;
    private int qtdRegistroPagina;
    private List<RegistrosDto> registros;

    public SimulacoesDto(int pagina, List<Simulacao> registros, int qtdRegistroPagina, Long qtdRegistros) {

        this.pagina = pagina;
        this.registros = registros.stream().map(RegistrosDto::new).collect(Collectors.toList());
        this.qtdRegistroPagina = qtdRegistroPagina;
        this.qtdRegistros = qtdRegistros;
    }
}
