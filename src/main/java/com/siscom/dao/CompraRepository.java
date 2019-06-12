package com.siscom.dao;

import com.siscom.dao.mapper.CompraRowMapper;
import com.siscom.dao.mapper.EstatisticaRowMapper;
import com.siscom.dao.mapper.ItemCompraRowMapper;
import com.siscom.service.model.Compra;
import com.siscom.service.model.Estatistica;
import com.siscom.service.model.ItemCompra;
import com.siscom.service.model.NomeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

@Repository
public class CompraRepository implements Serializable {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int excluirCompra(Integer numCompra){
        String deleteQuery = "DELETE FROM COMPRA WHERE NUMCOMPRA = ?";
        return jdbcTemplate.update(deleteQuery, new Object[] { numCompra });
    }

    public void fazerCompra(Compra compra) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertQuery = "INSERT INTO COMPRA(COD_FORNECEDOR, DATA) values (?,?)";
        String insertQueryItem = "INSERT INTO ITEM_COMPRA(CODIGO_COMPRA, CODIGO_PRODUTO, QUANTIDADE, VALOR_UNITARIO) values (?,?,?,?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, compra.getCodFornecedor());
            ps.setDate(2, new java.sql.Date(compra.getDataCompra().getTime()));
            return ps;
        }, keyHolder);

        for (ItemCompra itemCompra : compra.getCompraItens()) {
            jdbcTemplate.update(insertQueryItem, keyHolder.getKey(), itemCompra.getCodProduto(),
                    itemCompra.getQuantCompra(), itemCompra.getValorUnitario());

        }
    }

    public List<NomeData> obterListaCompras(String nomeFornecedor, Date de, Date para) {
        String nome = nomeFornecedor != null ? nomeFornecedor.toLowerCase() : "";

        String query = "SELECT C.NUMCOMPRA, P.NOME, C.Data FROM COMPRA C " +
                "INNER JOIN PESSOA P ON C.COD_FORNECEDOR = P.CODIGO " +
                "WHERE LOWER(P.NOME) LIKE ? AND DATA BETWEEN ? AND ? ORDER BY P.NOME ASC, C.DATA DESC";

        return jdbcTemplate.query(query, new Object[] { "%" + nome + "%", de, para }, new CompraRowMapper());
    }

    public List<Estatistica> buscarEstatisticaCompras(Date de, Date para) {
        String query = "SELECT P.NOME, (SELECT COUNT(1) FROM COMPRA CC WHERE CC.COD_FORNECEDOR = P.CODIGO) AS QTD_ACAO, SUM(IC.QUANTIDADE * IC.VALOR_UNITARIO) AS VALOR_TOTAL\n" +
                "FROM COMPRA C \n" +
                "INNER JOIN PESSOA P ON C.COD_FORNECEDOR = P.CODIGO\n" +
                "INNER JOIN ITEM_COMPRA IC ON C.NUMCOMPRA = IC.CODIGO_COMPRA\n" +
                "WHERE DATA BETWEEN ? AND ?\n" +
                "GROUP BY P.NOME\n" +
                "ORDER BY P.NOME ASC;";

        return jdbcTemplate.query(query, new Object[] { de, para }, new EstatisticaRowMapper());
    }

    public Integer buscarQtdCompras(Integer codPessoa) {
        String query = "SELECT COUNT(1) FROM COMPRA C WHERE C.COD_FORNECEDOR = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { codPessoa }, Integer.class);
    }

    public List<ItemCompra> buscarItens(Integer numCompra) {
        String query = "SELECT * FROM ITEM_COMPRA WHERE CODIGO_COMPRA = ?";
        return jdbcTemplate.query(query, new Object[] { numCompra }, new ItemCompraRowMapper());
    }

    public Integer buscarQtdProdutosCompra(Integer codProduto) {
        String query = "SELECT COUNT(1) FROM ITEM_COMPRA IC WHERE IC.CODIGO_PRODUTO = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { codProduto }, Integer.class);
    }

}
