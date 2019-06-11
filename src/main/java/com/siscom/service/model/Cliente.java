package com.siscom.service.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class Cliente extends Pessoa implements Serializable {

	private String cpf;
	private double limiteCredito;

	@Builder
	public Cliente(int codigo, String nome, String telefones, String email, Date dateCad, String cpf, double limiteCredito) {
		super(codigo, nome, telefones, email, dateCad);
		this.cpf = cpf;
		this.limiteCredito = limiteCredito;
	}

	public Cliente(){}

	public TipoPessoa informarTipoPessoa(){
		return TipoPessoa.CLIENTE;
	}

	@Override
	public String toString() {
		return "Cliente [cpf=" + cpf + ", limiteCredito=" + limiteCredito + "]";
	}

}
