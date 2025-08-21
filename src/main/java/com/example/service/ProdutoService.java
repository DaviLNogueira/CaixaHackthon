package com.example.service;

import com.example.model.domain.local.Parcela;
import com.example.model.domain.local.TipoEmprestimo;
import com.example.model.domain.local.Simulacao;
import com.example.model.domain.remoto.Produto;
import com.example.model.dto.PropostaDto;
import com.example.model.dto.RespostaPropostaDto;
import com.example.repository.ParcelaRepository;
import com.example.repository.SimulacaoRepository;
import com.example.repository.TipoEmprestimoRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProdutoService {

    @Inject
    SimulacaoRepository simulacaoRepository;

    @Inject
    TipoEmprestimoRepository tipoEmprestimoRepository;
    @Inject
    ParcelaRepository parcelaRepository;

    public RespostaPropostaDto getProduto(PropostaDto simulador) {
        Produto produto = Produto.find(
                "minimoMeses <= :prazo AND maximoMeses >= :prazo " +
                        "AND valorMinimo <= :valor AND valorMaximo >= :valor",
                Parameters.with("prazo", simulador.getPrazo())
                        .and("valor", simulador.getValorDesejado())
        ).firstResult();
        List<TipoEmprestimo> tipos = new ArrayList<>();
        tipos.add(calcularPrice(produto, simulador));
        tipos.add(calcularSac(produto, simulador));

        Simulacao simulacao = new Simulacao(produto.getId(), produto.getTaxaJuros());
        simulacao.addTipoEmprestimo(calcularPrice(produto, simulador));
        simulacao.addTipoEmprestimo(calcularSac(produto, simulador));
        salvarSimulacao(simulacao);
        return new RespostaPropostaDto(simulacao);

    }

    public Simulacao salvarSimulacao(Simulacao simulacao) {
        simulacaoRepository.persist(simulacao);
        return simulacao;
    }

//    public void test(){
//        Simulacao simulacao = new Simulacao(10,100);
//
//        simulacaoRepository.persist(simulacao);
//
//        TipoEmprestimo tipo = new TipoEmprestimo("SAC",simulacao);
//        tipo.setSimulacao(simulacao);
//        tipoEmprestimoRepository.persist(tipo);
//
//        Parcela parcela = new Parcela(1,100,10,10);
//        Parcela parcela1 = new Parcela(1,100,10,10);
//        parcela.setTipoEmprestimo(tipo);
//        parcela1.setTipoEmprestimo(tipo);
//        parcelaRepository.persist(parcela);
//        parcelaRepository.persist(parcela1);
//    }


    private TipoEmprestimo calcularSac(Produto produto, PropostaDto simulador) {
        TipoEmprestimo tipoEmprestimo = new TipoEmprestimo("SAC");
        int prazo = simulador.getPrazo();
        double amortizacaoMensal = simulador.getValorDesejado() / prazo;
        double saldoDevedor = simulador.getValorDesejado();
        for (int numeroParcela = 1; numeroParcela <= prazo; numeroParcela++) {
            double juros = saldoDevedor * produto.getTaxaJuros();
            double valorPrestacao = amortizacaoMensal + juros;
            saldoDevedor -= amortizacaoMensal;
            Parcela parcela = new Parcela(numeroParcela, valorPrestacao, juros, amortizacaoMensal);
            tipoEmprestimo.addParcela(parcela);
        }

        return tipoEmprestimo;
    }


    private TipoEmprestimo calcularPrice(Produto produto, PropostaDto simulador) {
        TipoEmprestimo tipoEmprestimo = new TipoEmprestimo("PRICE");

        double taxa = produto.getTaxaJuros();
        double totalPrestacao = simulador.getValorDesejado() * (taxa / (1 - Math.pow(1 + taxa, -simulador.getPrazo())));

        double saldoDevedor = simulador.getValorDesejado();

        for (int numeroParcela = 1; numeroParcela <= simulador.getPrazo(); numeroParcela++) {

            double valorJuros = saldoDevedor * taxa;
            double valorAmortizado = totalPrestacao - valorJuros;

            saldoDevedor -= valorAmortizado;
            Parcela parcela = new Parcela(numeroParcela, totalPrestacao, valorJuros, valorAmortizado);
            tipoEmprestimo.addParcela(parcela);

        }


        return tipoEmprestimo;
    }

}
