package com.siscom.controller.dto;

import com.siscom.service.model.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDto {
    private String nome;
    private String telefones;
    private String email;
    private Date dateCad;
    private String cliCpf;
    private Double cliLimiteCredito;
    private String forCnpj;
    private String forNomeContato;
    private String venCpf;
    private Double venMetaMensal;
    private TipoPessoa tipoPessoa;
}
