package com.siscom.dao;

import com.siscom.dao.mapper.ClienteRowMapper;
import com.siscom.dao.mapper.EstatisticaRowMapper;
import com.siscom.dao.mapper.ItemCompraRowMapper;
import com.siscom.dao.mapper.ItemVendaRowMapper;
import com.siscom.dao.mapper.NomeDataRowMapper;
import com.siscom.dao.mapper.VendedorRowMapper;
import com.siscom.service.model.Cliente;
import com.siscom.service.model.Estatistica;
import com.siscom.service.model.ItemCompra;
import com.siscom.service.model.ItemVenda;
import com.siscom.service.model.NomeData;
import com.siscom.service.model.TipoPessoa;
import com.siscom.service.model.Venda;
import com.siscom.service.model.Vendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
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

        String insertQuery = "INSERT INTO VENDA(COD_CLIENTE, COD_VENDEDOR, FORMAPGTO, DATA) values (?,?,?, ?)";
        String insertQueryItem = "INSERT INTO ITEM_VENDA(CODIGO_VENDA, CODIGO_PRODUTO, QUANTIDADE, VALOR_UNITARIO) values (?,?,?,?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, venda.getCliente().getCodigo());
            ps.setInt(2, venda.getVendedor().getCodigo());
            ps.setInt(3, venda.getFormaPagto());
            ps.setDate(4, new java.sql.Date(venda.getDataVenda().getTime()));
            return ps;
        }, keyHolder);

        for (ItemVenda itemVenda : venda.getVendaItens()) {
            jdbcTemplate.update(insertQueryItem, keyHolder.getKey(), itemVenda.getCodProduto(),
                    itemVenda.getQuantVenda(), itemVenda.getValorVenda());
        }
    }

    public List<NomeData> obterListaVendasCliente(String nomeCliente, java.util.Date de, java.util.Date para) {
        String nome = nomeCliente != null ? nomeCliente.toLowerCase() : "";

        String query = "SELECT V.NUMVENDA, P.NOME, V.Data FROM VENDA V INNER JOIN PESSOA P " +
                "ON V.COD_CLIENTE = P.CODIGO WHERE LOWER(P.NOME) LIKE ? AND DATA BETWEEN ? AND ? ORDER BY P.NOME ASC, V.DATA DESC";

        return jdbcTemplate.query(query, new Object[] { "%" + nome + "%", de, para }, new NomeDataRowMapper());
    }

    public List<NomeData> obterListaVendasVendedor(String nomeVendedor, java.util.Date de, java.util.Date para) {
        String nome = nomeVendedor != null ? nomeVendedor.toLowerCase() : "";

        String query = "SELECT V.NUMVENDA, P.NOME, V.Data  FROM VENDA V INNER JOIN PESSOA P " +
                "ON V.COD_VENDEDOR = P.CODIGO WHERE LOWER(P.NOME) LIKE ? AND DATA BETWEEN ? AND ? ORDER BY P.NOME ASC, V.DATA DESC";

        return jdbcTemplate.query(query, new Object[] { "%" + nome + "%", de, para }, new NomeDataRowMapper());
    }

    public List<Estatistica> obterEstatisticasVenda(java.util.Date de, java.util.Date para, TipoPessoa tipo) {
        String query = "";

        if (tipo == TipoPessoa.CLIENTE) {
            query = "SELECT P.NOME, (SELECT COUNT(1) FROM VENDA VV WHERE VV.COD_CLIENTE = P.CODIGO) AS QTD_ACAO, SUM(IV.QUANTIDADE * IV.VALOR_UNITARIO) AS VALOR_TOTAL\n" +
                    "FROM VENDA V \n" +
                    "INNER JOIN PESSOA P ON V.COD_CLIENTE = P.CODIGO\n" +
                    "INNER JOIN ITEM_VENDA IV ON V.NUMVENDA = IV.CODIGO_VENDA\n" +
                    "WHERE DATA BETWEEN ? AND ?\n" +
                    "GROUP BY P.NOME\n" +
                    "ORDER BY P.NOME ASC;";
        }

        if (tipo == TipoPessoa.VENDEDOR) {
            query = "SELECT P.NOME, (SELECT COUNT(1) FROM VENDA VV WHERE VV.COD_VENDEDOR = P.CODIGO) AS QTD_ACAO, SUM(IV.QUANTIDADE * IV.VALOR_UNITARIO) AS VALOR_TOTAL\n" +
                    "FROM VENDA V \n" +
                    "INNER JOIN PESSOA P ON V.COD_VENDEDOR = P.CODIGO\n" +
                    "INNER JOIN ITEM_VENDA IV ON V.NUMVENDA = IV.CODIGO_VENDA\n" +
                    "WHERE DATA BETWEEN ? AND ?\n" +
                    "GROUP BY P.NOME\n" +
                    "ORDER BY P.NOME ASC;";
        }

        return jdbcTemplate.query(query, new Object[] { de, para }, new EstatisticaRowMapper());
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
