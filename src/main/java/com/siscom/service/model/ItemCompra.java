package com.siscom.service.model;

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
public class ItemCompra {

	private Integer codProduto;
	private Integer quantCompra;
	private Double valorUnitario;

	@Override
	public String toString() {
		return "ItemCompra [codProduto=" + codProduto + ", quantCompra=" + quantCompra + ", valorUnitario=" + valorUnitario + "]";
	}

}