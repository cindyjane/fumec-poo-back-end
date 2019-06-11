package com.siscom.controller.mapper;

import com.siscom.controller.dto.PessoaDto;
import com.siscom.service.model.Cliente;
import com.siscom.service.model.Fornecedor;
import com.siscom.service.model.Pessoa;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Vendedor;

import java.util.Date;

public class PessoaMapper {

    public static Pessoa mapToModel(PessoaDto pessoa) {
        if (pessoa.getTipoPessoa() == TipoPessoa.CLIENTE) {

            return Cliente.builder()
                    .nome(pessoa.getNome())
                    .telefones(pessoa.getTelefones())
                    .email(pessoa.getEmail())
                    .cpf(pessoa.getCliCpf())
                    .limiteCredito(pessoa.getCliLimiteCredito())
                    .dateCad(new Date())
                    .build();
        }

        if (pessoa.getTipoPessoa() == TipoPessoa.VENDEDOR) {

            return Vendedor.builder()
                    .nome(pessoa.getNome())
                    .telefones(pessoa.getTelefones())
                    .email(pessoa.getEmail())
                    .dateCad(new Date())
                    .cpf(pessoa.getVenCpf())
                    .metaMensal(pessoa.getVenMetaMensal())
                    .build();
        }

        if (pessoa.getTipoPessoa() == TipoPessoa.FORNECEDOR) {

            return Fornecedor.builder()
                    .nome(pessoa.getNome())
                    .telefones(pessoa.getTelefones())
                    .email(pessoa.getEmail())
                    .dateCad(new Date())
                    .cnpj(pessoa.getForCnpj())
                    .nomeContato(pessoa.getForNomeContato())
                    .build();
        }

        return null;
    }
}
