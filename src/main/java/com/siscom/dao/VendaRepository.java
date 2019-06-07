package com.siscom.dao;

import com.siscom.dao.mapper.ClienteRowMapper;
import com.siscom.dao.mapper.EstatisticaRowMapper;
import com.siscom.dao.mapper.ItemCompraRowMapper;
import com.siscom.dao.mapper.ItemVendaRowMapper;
import com.siscom.service.model.Cliente;
import com.siscom.service.model.Estatistica;
import com.siscom.service.model.ItemCompra;
import com.siscom.service.model.ItemVenda;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VendaRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int excluirVenda(int numVenda) {
        String deleteQuery = "DELETE FROM VENDA WHERE NUMVENDA = ?";
        return jdbcTemplate.update(deleteQuery, new Object[] { numVenda });
    }

    public void fazerVenda(Venda venda) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertQuery = "INSERT INTO VENDA(COD_CLIENTE, COD_VENDEDOR, FORMAPGTO, DATA) values (?,?,?)";
        String insertQueryItem = "INSERT INTO ITEM_VENDA(CODIGO_VENDA, CODIGO_PRODUTO, QUANTIDADE, VALOR_UNITARIO) values (?,?,?,?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, venda.getCliente().getCodigo());
            ps.setInt(2, venda.getFormaPagto());
            ps.setDate(3, new java.sql.Date(venda.getDataVenda().getTime()));
            return ps;
        }, keyHolder);

        for (ItemVenda itemVenda : venda.getVendaItens()) {
            jdbcTemplate.update(insertQueryItem, keyHolder.getKey(), itemVenda.getCodProduto(),
                    itemVenda.getQuantVenda(), itemVenda.getValorVenda());
        }
    }

    public List<Cliente> obterListaVendasCliente(String nomeCliente, Date de, Date para) {
        String query = "SELECT P.NOME, V.Data, V.* FROM VENDA V INNER JOIN PESSOA P " +
                "ON V.COD_CLIENTE = P.CODIGO WHERE P.NOME LIKE '%?%' AND DATA BETWEEN ? AND ? ORDER BY P.NOME ASC, V.DATA DESC";

        return jdbcTemplate.query(query, new Object[] { nomeCliente, de, para }, new ClienteRowMapper());
    }

    public List<Venda> obterListaVendasVendedor(String nomeVendedor, Date de, Date para) {
        String query = "SELECT P.NOME, V.Data, V.* FROM VENDA V INNER JOIN PESSOA P " +
                "ON V.COD_VENDEDOR = P.CODIGO WHERE P.NOME LIKE '%?%' AND DATA BETWEEN ? AND ? ORDER BY P.NOME ASC, V.DATA DESC";

        //ToDo Criar VendedorRowMapper return jdbcTemplate.query(query, new Object[] { nomeVendedor, de, para }, new VendedorRowMapper());
        return null;
    }

    public List<Estatistica> obterEstatisticasVenda(Date de, Date para, TipoPessoa tipo) {
        String query = "SELECT P.NOME, COUNT(V.*) AS QTD_ACAO, SUM(IV.QUANTIDADE * IV.VALOR_UNITARIO) AS VALOR_TOTAL\n" +
                "FROM VENDA V \n" +
                "INNER JOIN PESSOA P ON C.COD_FORNECEDOR = P.CODIGO\n" +
                "INNER JOIN ITEM_VENDA IV ON V.NUMVENDA = IV.CODIGO_VENDA\n" +
                "WHERE DATA BETWEEN ? AND ? AND P.TIPO_PESSOA = ?\n" +
                "GROUP BY P.NOME\n" +
                "ORDER BY P.NOME ASC;";

        return jdbcTemplate.query(query, new Object[] { de, para, tipo }, new EstatisticaRowMapper());
    }

    public List<ItemVenda> buscarItens(Integer numVenda) {
        String query = "SELECT * FROM ITEM_VENDA WHERE CODIGO_VENDA = ?";
        return jdbcTemplate.query(query, new Object[] { numVenda }, new ItemVendaRowMapper());
    }

    public Integer buscarQtdVendasCliente(Integer codPessoa) {
        String query = "SELECT COUNT(1) FROM VENDA V WHERE V.COD_CLIENTE = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { codPessoa }, Integer.class);
    }

    public Integer buscarQtdVendasVendedor(Integer codPessoa) {
        String query = "SELECT COUNT(1) FROM VENDA V WHERE V.COD_VENDEDOR = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { codPessoa }, Integer.class);
    }

    public Integer buscarQtdProdutosVenda(Integer codProduto) {
        String query = "SELECT COUNT(1) FROM ITEM_VENDA IV WHERE IV.CODIGO_PRODUTO = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { codProduto }, Integer.class);
    }

}
