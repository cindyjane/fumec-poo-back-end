package com.siscom.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDto {
    private String nome;
    private Double precoUnitario;
    private Integer estoque;
    private Integer estoqueMinimo;
}
