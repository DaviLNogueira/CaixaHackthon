package com.example.model.dto;

import com.example.model.domain.local.TipoEmprestimo;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TipoEmprestimoDto {  // REMOVE extends PanacheEntityBase

    private String tipo;
    private List<ParcelaDto> parcelas;

    public TipoEmprestimoDto(TipoEmprestimo tipoEmprestimo) {
        this.tipo = tipoEmprestimo.getTipo();
        this.parcelas = tipoEmprestimo.getParcelas().stream()
                .map(ParcelaDto::new)
                .collect(Collectors.toList());
    }

}