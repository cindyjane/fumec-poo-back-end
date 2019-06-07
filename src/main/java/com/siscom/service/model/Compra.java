package com.siscom.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compra implements Serializable {

	private Integer numCompra;
	private Integer codFornecedor;
	private List<ItemCompra> compraItens;
	private Date dataCompra;

	@Override
	public String toString() {
		return "Compra{" +
				"numCompra=" + numCompra +
				", codFornecedor=" + codFornecedor +
				", compraItens=" + compraItens +
				", dataCompra=" + dataCompra +
				'}';
	}
}
