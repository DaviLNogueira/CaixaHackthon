package com.example.service;

import com.example.model.domain.local.RequisicaoLog;
import com.example.model.dto.TelemetricaDto;
import com.example.model.dto.TelemetricaPorData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RequisicaotLogService {

    @Transactional
    public void persist(RequisicaoLog log) {
        log.persist();
    }



    public TelemetricaPorData getTelemetricaPorData(Date dateTime) {
        LocalDateTime inicio = LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDay(),0,0,0 );
        LocalDateTime fim = LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDay(),23,59,59);

        List<RequisicaoLog> logs = RequisicaoLog.getEntityManager().createQuery(
                        "SELECT l FROM RequisicaoLog l WHERE l.dataReferencia BETWEEN :inicio AND :fim",
                        RequisicaoLog.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();

        List<TelemetricaDto>  dados = new ArrayList<>(logs.stream()
                .collect(Collectors.groupingBy(
                        RequisicaoLog::getRota,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            long total = list.size();
                            long sucessos = list.stream().filter(RequisicaoLog::isSucesso).count();
                            DoubleSummaryStatistics stats = list.stream()
                                    .mapToDouble(RequisicaoLog::getDuracao)
                                    .summaryStatistics();

                            return new TelemetricaDto(
                                    list.get(0).getRota(),
                                    (int) total,
                                    stats.getAverage(),
                                    stats.getMin(),
                                    stats.getMax(),
                                    sucessos
                            );
                        })
                ))
                .values());

       return new TelemetricaPorData(dateTime.toString(),dados);
    }
}
