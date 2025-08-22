package com.example.service;

import com.example.model.domain.local.Simulacao;
import com.example.model.dto.EstatisticaDto;
import com.example.model.dto.SimulacoesDto;
import com.example.model.dto.VolumeDto;
import com.example.repository.SimulacaoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class ListagemService {

    @Inject
    SimulacaoRepository simulacaoRepository;


    public SimulacoesDto listarSimulacoes(int pagina, int qtdRegistros) {
        PanacheQuery<Simulacao> query = simulacaoRepository.findAll().page(pagina, qtdRegistros);

        //TODO:resolver depois pq não está listando
        List<Simulacao> registros = query.list().stream().toList();
        long totalRegistros = Simulacao.count();

        return new SimulacoesDto(
                pagina,
                registros,
                qtdRegistros,
                totalRegistros
        );
    }

    public VolumeDto estatisticasSimulacao(Date dataReferencia) {
        List<Integer> codigosProdutos = simulacaoRepository
                .find("SELECT DISTINCT s.codigoProduto FROM Simulacao s WHERE CAST(s.dataReferencia AS DATE) = CAST(?1 AS DATE)", dataReferencia)
                .project(Integer.class)
                .list();


        List<EstatisticaDto> estatistica = new ArrayList<>();

        for (Integer codigoProduto : codigosProdutos) {
            List<Simulacao> simulacoes = simulacaoRepository
                    .find("codigoProduto = ?1", codigoProduto)
                    .list();


            double mediaTaxaJuros = simulacoes.stream()
                    .mapToDouble(Simulacao::getTaxaJuros)
                    .average()
                    .orElse(0);

            double mediaValorMedioPrestacao = simulacoes.stream()
                    .mapToDouble(Simulacao::getValorMedioPrestacao)
                    .average()
                    .orElse(0);

            mediaTaxaJuros = Double.parseDouble(String.format(Locale.US, "%.3f", mediaTaxaJuros));
            mediaValorMedioPrestacao = Double.parseDouble(String.format(Locale.US, "%.2f", mediaValorMedioPrestacao));


            double somaValorDesejado = simulacoes.stream()
                    .mapToDouble(Simulacao::getValorDesejado)
                    .sum();

            double somaValorCredito = simulacoes.stream()
                    .mapToDouble(Simulacao::getValorTotalCredito)
                    .sum();

            estatistica.add(new EstatisticaDto(
                    codigoProduto,
                    mediaTaxaJuros,
                    somaValorCredito,
                    somaValorDesejado,
                    mediaValorMedioPrestacao
            ));
        }


        return new VolumeDto(dataReferencia,estatistica);

    }
}
