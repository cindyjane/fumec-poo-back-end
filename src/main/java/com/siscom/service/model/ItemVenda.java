package com.siscom.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemVenda {

	private Integer codProduto;
	private int quantVenda;
	private double valorVenda;

	@Override
	public String toString() {
		return "ItemVenda [codProduto=" + codProduto + ", quantVenda=" + quantVenda + ", valorVenda=" + valorVenda + "]";
	}
	
}
