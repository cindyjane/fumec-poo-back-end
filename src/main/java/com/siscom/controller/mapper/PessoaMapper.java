package com.siscom.controller.mapper;

import com.siscom.controller.dto.PessoaDto;
import com.siscom.service.model.Cliente;
import com.siscom.service.model.Fornecedor;
import com.siscom.service.model.Pessoa;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Vendedor;

public class PessoaMapper {

    public static Pessoa mapToModel(PessoaDto pessoa) {
        if (pessoa.getTipoPessoa() == TipoPessoa.CLIENTE) {

            return Cliente.builder().cpf(pessoa.getCliCpf()).build();
        }

        if (pessoa.getTipoPessoa() == TipoPessoa.VENDEDOR) {

            return Vendedor.builder().build();
        }

        if (pessoa.getTipoPessoa() == TipoPessoa.FORNECEDOR) {

            return Fornecedor.builder().build();
        }

        return null;
    }
}
