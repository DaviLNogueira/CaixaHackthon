package com.example.service;

import com.example.model.domain.local.RequisicaoLog;
import com.example.model.dto.TelemetricaDto;
import com.example.model.dto.TelemetricaPorData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.*;

@ApplicationScoped
public class RequisicaotLogService {

    @Transactional
    public void persist(RequisicaoLog log) {
        log.persist();
    }


    public TelemetricaPorData getTelemetricaPorData(Date dataReferencia) {
        List<String> rotas = RequisicaoLog
                .find("SELECT DISTINCT s.rota FROM RequisicaoLog s WHERE CAST(s.dataReferencia AS DATE) = CAST(?1 AS DATE)", dataReferencia)
                .project(String.class)
                .list();


        List<TelemetricaDto> telemetria = new ArrayList<>();

        for (String rota : rotas) {
            List<RequisicaoLog> requisicaoLog = RequisicaoLog
                    .find("rota = ?1 and CAST(dataReferencia AS DATE) = CAST(?2 AS DATE)", rota, dataReferencia)
                    .list();


            double tempoMedio = requisicaoLog.stream()
                    .mapToDouble(RequisicaoLog::getDuracao)
                    .average()
                    .orElse(0);

            double tempoMinimo = requisicaoLog.stream()
                    .mapToDouble(RequisicaoLog::getDuracao)
                    .min()
                    .orElse(0);


            double tempoMaximo = requisicaoLog.stream()
                    .mapToDouble(RequisicaoLog::getDuracao)
                    .max().orElse(0);


            long quantidadeReq = requisicaoLog.size();

            long sucesso = requisicaoLog.stream().filter(RequisicaoLog::isSucesso).count();


            telemetria.add(
                    new TelemetricaDto(
                            rota,
                            quantidadeReq,
                            tempoMedio,
                            tempoMinimo,
                            tempoMaximo,
                            (double) sucesso  / quantidadeReq
                    )
            );

        }

        return new TelemetricaPorData(dataReferencia, telemetria);
    }
}
