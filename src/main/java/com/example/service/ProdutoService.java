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

    public RespostaPropostaDto getProduto(PropostaDto proposta)  {
        Produto produto = Produto.find(
                "minimoMeses <= :prazo AND maximoMeses >= :prazo ",
                Parameters.with("prazo", proposta.getPrazo())
        ).firstResult();
//        if (proposta.getValorDesejado() < produto.getValorMinimo() || proposta.getValorDesejado() > produto.getValorMaximo() ) {
//            throw new Exception("Para este prazo o valor desejado deve estar entre " + produto.getValorMinimo() + " e " + produto.getValorMaximo());
//        }
        List<TipoEmprestimo> tipos = new ArrayList<>();
        tipos.add(calcularPrice(produto, proposta));
        tipos.add(calcularSac(produto, proposta));

        Simulacao simulacao = new Simulacao(produto, proposta);
        simulacao.addTipoEmprestimo(calcularPrice(produto, proposta));
        simulacao.addTipoEmprestimo(calcularSac(produto, proposta));
        salvarSimulacao(simulacao);
        return new RespostaPropostaDto(simulacao);

    }

    public Simulacao salvarSimulacao(Simulacao simulacao) {
        simulacaoRepository.persist(simulacao);
        return simulacao;
    }


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
