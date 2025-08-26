package com.example.service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.example.exception.ApiException;
import com.example.model.domain.local.Parcela;
import com.example.model.domain.local.TipoEmprestimo;
import com.example.model.domain.local.Simulacao;
import com.example.model.domain.remoto.Produto;
import com.example.model.dto.PropostaDto;
import com.example.model.dto.RespostaPropostaDto;
import com.example.repository.SimulacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProdutoService {

    @Inject
    SimulacaoRepository simulacaoRepository;

    private final ObjectMapper mapper = new ObjectMapper();

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

    //TODO padronizar método de busca
    public RespostaPropostaDto realizarSimulacao(PropostaDto proposta) throws Exception {
        //TODO tratar quandonão encontrar produto
        Produto produto = Produto.find(
                "minimoMeses <= :prazo AND maximoMeses >= :prazo ",
                Parameters.with("prazo", proposta.getPrazo())
        ).firstResult();
        if (proposta.getValorDesejado() < produto.getValorMinimo() || proposta.getValorDesejado() > produto.getValorMaximo() ) {
            throw new ApiException(String.format(
                    "Para este prazo o valor desejado deve estar entre %s e %s",
                    produto.getValorMinimo(), produto.getValorMaximo()));
        }
        List<TipoEmprestimo> tipos = new ArrayList<>();
        tipos.add(calcularPrice(produto, proposta));
        tipos.add(calcularSac(produto, proposta));

        Simulacao simulacao = new Simulacao(produto, proposta);
        simulacao.addTipoEmprestimo(calcularPrice(produto, proposta));
        simulacao.addTipoEmprestimo(calcularSac(produto, proposta));
        simulacaoRepository.persist(simulacao);
        RespostaPropostaDto resposta = new RespostaPropostaDto(simulacao, produto);
//        enviarDadosEventHub(resposta);
        return resposta;

    }

    public void enviarDadosEventHub(RespostaPropostaDto respostaPropostaDto) throws ApiException {
        //TODO melhor código
        try {
            String json = mapper.writeValueAsString(respostaPropostaDto);

            EventHubProducerClient producer = new EventHubClientBuilder()
                    .connectionString(
                            "Endpoint=sb://eventhack.servicebus.windows.net/;" +
                                    "SharedAccessKeyName=hack;" +
                                    "SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=;" + // <-- faltava o "="
                                    "EntityPath=simulacoes")
                    .buildProducerClient();

            EventDataBatch batch = producer.createBatch();
            batch.tryAdd(new EventData(json.getBytes(StandardCharsets.UTF_8)));

            producer.send(batch);

            producer.close();

        } catch (Exception e) {
            throw new ApiException(String.format("Erro ao enviar dados para o Event Hub: %s",e.getMessage()));
        }
    }




}
