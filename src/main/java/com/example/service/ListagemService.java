package com.example.service;

import com.example.model.domain.local.Simulacao;
import com.example.model.dto.SimulacoesDto;
import com.example.repository.SimulacaoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListagemService {

    @Inject
    SimulacaoRepository simulacaoRepository;


    public SimulacoesDto listarSimulacoes(int pagina, int qtdRegistros) {
        PanacheQuery<Simulacao> query = simulacaoRepository.findAll().page(pagina, qtdRegistros);

        //TODO:resolver depois pq não está listando
        List<Simulacao> registros = query.list().stream().toList();
        long totalRegistros = Simulacao.count();

        // Verifique a ordem dos parâmetros do seu construtor!
        return new SimulacoesDto(
                pagina,          // List<Simulacao> registros
                registros,             // int paginaAtual
                qtdRegistros,       // int quantidadePorPagina
                totalRegistros      // long totalRegistros
        );
    }
}
