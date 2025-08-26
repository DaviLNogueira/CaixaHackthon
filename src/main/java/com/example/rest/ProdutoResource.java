package com.example.rest;

import com.example.exception.ApiException;
import com.example.model.domain.local.RequisicaoLog;
import com.example.model.dto.*;
import com.example.service.ListagemService;
import com.example.service.ProdutoService;
import com.example.service.RequisicaotLogService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/produto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {



    @Inject
    ProdutoService produtoService;

    @Inject
    ListagemService listagemService;

    @Inject
    RequisicaotLogService  requisicoesLogService;

    @POST
    @Path("/nova-simulacao")
    public RespostaPropostaDto realizarSimulacao(@Valid PropostaDto proposta) throws Exception {

        if (proposta == null) {
            throw new ApiException("Os parametros devem ser informados");
        }
        return produtoService.realizarSimulacao(proposta);
    }

    @GET
    @Path("/listar-simulacoes")
    public SimulacoesDto listarSimulacoes(
            @QueryParam("pagina") @DefaultValue("1") @Positive(message = "O campo pagina deve ser maior que 0") int pagina,
            @QueryParam("qtdRegistrosPagina") @DefaultValue("10") int qtdRegistros) {
        return listagemService.listarSimulacoes(pagina, qtdRegistros);
    }

    @GET
    @Path("/estatistica")
    public VolumeDto listarEstatistica(@QueryParam("dataReferencia") String dataReferencia) throws ParseException {
        Date data = new Date();
        if(dataReferencia != null){
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false);
            data =  formato.parse(dataReferencia);
        }
        return listagemService.estatisticasSimulacao(data);
    }

    @GET
    @Path("/telemetria")
    public TelemetricaPorData getTelemetricaPorData(@QueryParam("dataReferencia") String dataReferencia) {
        Date data = new Date();
        if(dataReferencia != null && !dataReferencia.isEmpty()){
            data = new Date(Long.parseLong(dataReferencia));
        }
        return requisicoesLogService.getTelemetricaPorData(data);
    }

    @GET
    @Path("/logs")
    public List<RequisicaoLog> all() {
        return RequisicaoLog.listAll();
    }



}
