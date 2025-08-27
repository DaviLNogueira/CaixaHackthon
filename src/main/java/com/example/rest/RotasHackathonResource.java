package com.example.rest;

import com.example.exception.ApiException;
import com.example.model.dto.*;
import com.example.service.ListagemService;
import com.example.service.ProdutoService;
import com.example.service.RequisicaotLogService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Path("/servicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(
        info = @Info(
                title = "HACKATHON - CAIXA 2025",
                description = "Simulação de Crédito",
                version = "1.0.0")
)
public class RotasHackathonResource {


    @Inject
    ProdutoService produtoService;

    @Inject
    ListagemService listagemService;

    @Inject
    RequisicaotLogService requisicoesLogService;

    @Operation(
            summary = "Serviço para solicitação de simulação de crédito"
    )
    @POST
    @Path("/nova-simulacao")
    public RespostaPropostaDto realizarSimulacao(@Valid PropostaDto proposta) throws Exception {

        if (proposta == null) {
            throw new ApiException("Os parametros devem ser informados");
        }
        return produtoService.realizarSimulacao(proposta);
    }

    @Operation(
            summary = "Serviço para retornar todas as simulações",
            description = "Utiliza de paginação para retornar os dados(caso não respondida assumirá valores padrões)"
    )
    @GET
    @Path("/listar-simulacoes")
    public SimulacoesDto listarSimulacoes(
            @QueryParam("pagina") @DefaultValue("1") @Positive(message = "O campo pagina deve ser maior que 0") int pagina,
            @QueryParam("qtdRegistrosPagina") @DefaultValue("10") int qtdRegistros) {
        return listagemService.listarSimulacoes(pagina, qtdRegistros);
    }

    @Operation(
            summary = "Serviço para retornar os valores simulados para cada produto em cada dia",
            description = "Utiliza de parametro de data informada na query de requisição(caso não respondida assumirá valores padrão)"
    )
    @GET
    @Path("/dados-simulacoes")
    public VolumeDto listarEstatistica(
            @Parameter(
                    description = "Data no formato YYYY-MM-DD",
                    schema = @Schema(
                            pattern = "^\\d{4}-\\d{2}-\\d{2}$",
                            implementation = String.class
                    ))
            @QueryParam("dataReferencia") String dataReferencia) throws ParseException {
        Date data = new Date();
        if (dataReferencia != null) {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false);
            data = formato.parse(dataReferencia);
        }
        return listagemService.estatisticasSimulacao(data);
    }

    @Operation(
            summary = "Serviço para retornar dados de telemetria com volumes e tempos de resposta de cada serviço",
            description = "Utiliza de parametro de data informada na query de requisição(caso não respondida assumirá valores padrões)"
    )
    @GET
    @Path("/telemetria")
    public TelemetricaPorData getTelemetricaPorData(
            @Parameter(
                    description = "Data no formato YYYY-MM-DD",
                    schema = @Schema(
                            pattern = "^\\d{4}-\\d{2}-\\d{2}$",
                            implementation = String.class
                    )
            ) @QueryParam("dataReferencia") String dataReferencia) throws ParseException {
        Date data = new Date();
        if (dataReferencia != null && !dataReferencia.isEmpty()) {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false);
            data = formato.parse(dataReferencia);
        }
        return requisicoesLogService.getTelemetricaPorData(data);
    }


}
