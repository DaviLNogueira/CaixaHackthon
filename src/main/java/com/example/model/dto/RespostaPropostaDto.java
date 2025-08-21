package com.example.model.dto;

import com.example.model.domain.local.Simulacao;
import com.example.model.domain.remoto.Produto;
import com.example.model.domain.local.TipoEmprestimo;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter @Getter
public class RespostaPropostaDto extends PanacheEntityBase {

    private Long idSimulacao;
    private int codigoProduto;
    private String descricaoProduto;
    private double taxaJuros;
    private List<TipoEmprestimoDto> resultadoSimulacao;


    public RespostaPropostaDto(Simulacao  simulacao) {
        //TODO : salvar simulacao
        this.idSimulacao = simulacao.getIdSimulacao();
        this.codigoProduto = simulacao.getCodigoProduto();
//        this.descricaoProduto = produto.getNome();
        this.taxaJuros = simulacao.getTaxaJuros();
        this.resultadoSimulacao = simulacao.getTipoEmprestimos().stream()
                .map(TipoEmprestimoDto::new)  // Method reference - mais limpo
                .collect(Collectors.toList());
    }

}
