package com.siscom.service.model;


import com.siscom.exception.SisComException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Produto {
	
	private int codigo;
	private String nome;
	private double precoUnitario;
	private int estoque;
	private int estoqueMinimo;
	private Date dateCad;

	@Override
	public String toString() {
		return "Produto [codigo=" + codigo + ", nome=" + nome + ", precoUnitario=" + precoUnitario + ", estoque="
				+ estoque + ", estoqueMinimo=" + estoqueMinimo + ", dateCad=" + dateCad + "]";
	}
	public Produto(int codigo, String nome, double precoUnitario, int estoque, int estoqueMinimo, Date dateCad) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.precoUnitario = precoUnitario;
		this.estoque = estoque;
		this.estoqueMinimo = estoqueMinimo;
		this.dateCad = dateCad;
	}
	public Produto(){}
	
	
	public void addEstoque(int quantidade){
		this.estoque+=quantidade;

	}

	public void removeEstoque(int quantidade) throws SisComException {
		if(estoque - quantidade < 0){
			throw new SisComException(this.nome,this.estoque,"Estoque Insuficiente");
		}else {
			this.estoque -= quantidade;
		}
	}


    public int compareTo(Produto produto) {
        if(this.nome == produto.getNome()){
            return 0;
        }else if(this.nome.length() < produto.getNome().length()){
            return -1;
        }else{
            return 1;
        }
    }
}
