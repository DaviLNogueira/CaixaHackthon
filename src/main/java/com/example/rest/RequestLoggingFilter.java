package com.example.rest;

import com.example.model.domain.local.RequisicaoLog;
import com.example.service.RequisicaotLogService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.inject.Inject;

import java.io.IOException;
import java.time.LocalDateTime;

@Provider
public class RequestLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    RequisicaotLogService requisicoesLogService;

    private static final String INICIO_REQ = "start-time";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(INICIO_REQ, System.nanoTime());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object inicoObj = requestContext.getProperty(INICIO_REQ);
        if (inicoObj == null) {
            return;
        }

        long inicio = (long) inicoObj;
        long duracao = (System.nanoTime() - inicio) / 1_000_000;

        RequisicaoLog log = new RequisicaoLog();
        log.rota = requestContext.getUriInfo().getPath();
        log.duracao = duracao;
        log.sucesso = responseContext.getStatus() < 400;
        log.dataReferencia = LocalDateTime.now();

        requisicoesLogService.persist(log);
    }

}
