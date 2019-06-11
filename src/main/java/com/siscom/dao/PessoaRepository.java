package com.siscom.dao;

import com.siscom.dao.mapper.ClienteRowMapper;
import com.siscom.dao.mapper.FornecedorRowMapper;
import com.siscom.dao.mapper.PessoaRowMapper;
import com.siscom.dao.mapper.VendedorRowMapper;
import com.siscom.service.model.Cliente;
import com.siscom.service.model.Fornecedor;
import com.siscom.service.model.Pessoa;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Vendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Null;

import java.util.List;

@Repository
public class PessoaRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int excluirPessoa(Integer codigo) {
        String deleteQuery = "DELETE FROM PESSOA WHERE CODIGO = ?";
        return jdbcTemplate.update(deleteQuery, new Object[] { codigo });
    }

    public void addCliente(Cliente cliente) {
        String insertQuery = "INSERT INTO PESSOA(NOME, TELEFONES, EMAIL, DATECAD, CLI_CPF, CLI_LIMITECREDITO, TIPO_PESSOA) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(insertQuery, cliente.getNome(), cliente.getTelefones(), cliente.getEmail(),
                cliente.getDateCad(), cliente.getCpf(), cliente.getLimiteCredito(), TipoPessoa.CLIENTE.getCodigo());
    }

    public void addFornecedor(Fornecedor fornecedor) {
        String insertQuery = "INSERT INTO PESSOA(NOME, TELEFONES, EMAIL, DATECAD, FOR_CNPJ, FOR_NOMECONTATO, TIPO_PESSOA) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(insertQuery, fornecedor.getNome(), fornecedor.getTelefones(), fornecedor.getEmail(),
                fornecedor.getDateCad(), fornecedor.getCnpj(), fornecedor.getNomeContato(),
                TipoPessoa.FORNECEDOR.getCodigo());
    }

    public void addVendedor(Vendedor vendedor) {
        String insertQuery = "INSERT INTO PESSOA(NOME, TELEFONES, EMAIL, DATECAD, VEN_CPF, VEN_METAMENSAL, TIPO_PESSOA) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(insertQuery, vendedor.getNome(), vendedor.getTelefones(), vendedor.getEmail(),
                vendedor.getDateCad(), vendedor.getCpf(), vendedor.getMetaMensal(), TipoPessoa.VENDEDOR.getCodigo());
    }

    public List<Cliente> buscarPessoasOrdemAlfabetica(String nomePessoa, @Null TipoPessoa tipo) {
        String query = "SELECT * FROM PESSOA WHERE LOWER(NOME) LIKE ? AND TIPO_PESSOA = ISNULL(?, TIPO_PESSOA) ORDER BY NOME";
        return jdbcTemplate.query(query, new Object[] { "%" + nomePessoa.toLowerCase() + "%", tipo != null ? tipo.getCodigo(): null },
                new ClienteRowMapper());
    }

    public Pessoa buscarPessoa(Integer codPessoa) {
        String buscarQuery = "SELECT * FROM PESSOA WHERE CODIGO = ?";
        try {
            return jdbcTemplate.queryForObject(buscarQuery, new Object[] { codPessoa }, new PessoaRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Cliente buscarCliente(String cpf) {
        String buscarQuery = "SELECT * FROM PESSOA WHERE CLI_CPF = ? AND TIPO_PESSOA = ?";
        try {
            return jdbcTemplate.queryForObject(buscarQuery, new Object[] { cpf, TipoPessoa.CLIENTE.getCodigo() },
                    new ClienteRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Fornecedor buscarFornecedor(String cnpj) {
        String buscarQuery = "SELECT * FROM PESSOA WHERE FOR_CNPJ = ? AND TIPO_PESSOA = ?";
        try {
            return jdbcTemplate.queryForObject(buscarQuery, new Object[] { cnpj, TipoPessoa.FORNECEDOR.getCodigo() },
                    new FornecedorRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Vendedor buscarVendedor(String cpf) {
        String buscarQuery = "SELECT * FROM PESSOA WHERE VEN_CPF = ? AND TIPO_PESSOA = ?";
        try {
            return jdbcTemplate.queryForObject(buscarQuery, new Object[] { cpf, TipoPessoa.VENDEDOR.getCodigo() },
                    new VendedorRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;

        }
    }
}