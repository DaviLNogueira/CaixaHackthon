package com.example.rest;

import com.example.model.dto.*;
import com.example.service.ListagemService;
import com.example.service.ProdutoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;

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

    @POST
    @Path("/all")
    public RespostaPropostaDto listarTodosProdutos(PropostaDto simulador)  {
        return produtoService.getProduto(simulador);
    }

    @GET
    @Path("/listar-simulacoes")
    public SimulacoesDto listarSimulacoes(
            @QueryParam("pagina") @DefaultValue("1") int pagina,
            @QueryParam("qtdRegistrosPagina") @DefaultValue("10") int qtdRegistros) {
        return listagemService.listarSimulacoes(pagina, qtdRegistros);
    }

    @GET
    @Path("/estatistica")
    public VolumeDto listarEstatistica(@QueryParam("dataReferencia") String dataReferencia) {
        Date data = new Date();
        if(dataReferencia != null && !dataReferencia.isEmpty()){
            data = new Date(Long.parseLong(dataReferencia));
        }
        return listagemService.estatisticasSimulacao(data);
    }



}
