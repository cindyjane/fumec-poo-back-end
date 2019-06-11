package com.siscom.dao.mapper;

import com.siscom.service.model.Cliente;
import com.siscom.service.model.Fornecedor;
import com.siscom.service.model.Pessoa;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Vendedor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PessoaRowMapper implements RowMapper<Pessoa> {

    @Override
    public Pessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer tipo = rs.getInt("TIPO_PESSOA");

        if (tipo == TipoPessoa.CLIENTE.getCodigo()) {

            Cliente cliente = new Cliente();

            cliente.setCodigo(rs.getInt("CODIGO"));
            cliente.setNome(rs.getString("NOME"));
            cliente.setTelefones(rs.getString("TELEFONES"));
            cliente.setEmail(rs.getString("EMAIL"));
            cliente.setDateCad(rs.getDate("DATECAD"));
            cliente.setCpf(rs.getString("CLI_CPF"));
            cliente.setLimiteCredito(rs.getDouble("CLI_LIMITECREDITO"));

            return cliente;
        }

        if (tipo == TipoPessoa.FORNECEDOR.getCodigo()) {
            Fornecedor fornecedor = new Fornecedor();

            fornecedor.setCodigo(rs.getInt("CODIGO"));
            fornecedor.setNome(rs.getString("NOME"));
            fornecedor.setTelefones(rs.getString("TELEFONES"));
            fornecedor.setEmail(rs.getString("EMAIL"));
            fornecedor.setDateCad(rs.getDate("DATECAD"));
            fornecedor.setCnpj(rs.getString("FOR_CNPJ"));
            fornecedor.setNomeContato(rs.getString("FOR_NOMECONTATO"));

            return fornecedor;
        }

        if (tipo == TipoPessoa.VENDEDOR.getCodigo()) {
            Vendedor vendedor = new Vendedor();

            vendedor.setCodigo(rs.getInt("CODIGO"));
            vendedor.setNome(rs.getString("NOME"));
            vendedor.setTelefones(rs.getString("TELEFONES"));
            vendedor.setEmail(rs.getString("EMAIL"));
            vendedor.setDateCad(rs.getDate("DATECAD"));
            vendedor.setCpf(rs.getString("VEN_CPF"));
            vendedor.setMetaMensal(rs.getDouble("VEN_METAMENSAL"));

            return vendedor;
        }

        return null;
    }
}
