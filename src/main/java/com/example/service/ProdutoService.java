package com.example.service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.models.CreateBatchOptions;
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

//    private EventHubProducerClient producer;

//    public void EventHubService() {
//        this.producer = new EventHubClientBuilder()
//                .connectionString(
//                        "Endpoint=sb://eventhack.servicebus.windows.net/;" +
//                                "SharedAccessKeyName=hack;" +
//                                "SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2ILh/4MuDo4y+AEhkp8z+g;" +
//                                "EntityPath=simulacoes")
//                .buildProducerClient();
//    }
//
//    public void enviarJsons(List<String> jsons) {
//        CreateBatchOptions options = new CreateBatchOptions().setPartitionId("0");
//        EventDataBatch batch = producer.createBatch(options);
//
//        for (String json : jsons) {
//            if (!batch.tryAdd(new EventData(json))) {
//                producer.send(batch);
//                batch = producer.createBatch(options);
//                batch.tryAdd(new EventData(json));
//            }
//        }
//
//        producer.send(batch);
//    }

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
        RespostaPropostaDto resposta = new RespostaPropostaDto(simulacao);
        salvarSimulacao(simulacao,resposta);
        return resposta;

    }

    public Simulacao salvarSimulacao(Simulacao simulacao, RespostaPropostaDto respostaPropostaDto) {
        simulacaoRepository.persist(simulacao);
        try {
            String json = mapper.writeValueAsString(respostaPropostaDto);

            // 3. Cria o cliente do Event Hub
            EventHubProducerClient producer = new EventHubClientBuilder()
                    .connectionString(
                            "Endpoint=sb://eventhack.servicebus.windows.net/;" +
                                    "SharedAccessKeyName=hack;" +
                                    "SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=;" + // <-- faltava o "="
                                    "EntityPath=simulacoes")
                    .buildProducerClient();

            // 4. Cria batch e adiciona evento
            EventDataBatch batch = producer.createBatch();
            batch.tryAdd(new EventData(json.getBytes(StandardCharsets.UTF_8)));

            // 5. Envia para o Event Hub
            producer.send(batch);

            // 6. Fecha o cliente
            producer.close();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar dados para o Event Hub", e);
            // Aqui você pode logar o erro ou lançar uma exception custom
        }
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
