package com.example.rest;

import com.example.model.dto.PropostaDto;
import com.example.model.dto.RespostaPropostaDto;
import com.example.service.ProdutoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/produto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {



    @Inject
    ProdutoService produtoService;

    @POST
    @Path("/all")
    public RespostaPropostaDto listarTodosProdutos(PropostaDto simulador) {
        return produtoService.getProduto(simulador);
    }


    @POST
    @Path("/simulacao")
    public void teste(){
        produtoService.test();
    }


}
