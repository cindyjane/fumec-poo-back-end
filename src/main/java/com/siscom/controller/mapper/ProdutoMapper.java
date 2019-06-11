package com.siscom.controller.mapper;

import com.siscom.controller.dto.ProdutoDto;
import com.siscom.service.model.Produto;

import java.util.Date;

public class ProdutoMapper {

    public static Produto mapToModel(ProdutoDto produto) {
        return Produto.builder()
                .nome(produto.getNome())
                .precoUnitario(produto.getPrecoUnitario())
                .estoque(produto.getEstoque())
                .estoqueMinimo(produto.getEstoqueMinimo())
                .dateCad(new Date())
                .build();
    }
}
