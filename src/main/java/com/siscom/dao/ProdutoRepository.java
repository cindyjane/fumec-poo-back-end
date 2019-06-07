package com.siscom.dao;

import com.siscom.dao.mapper.ClienteRowMapper;
import com.siscom.dao.mapper.ProdutoRowMapper;
import com.siscom.service.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProdutoRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int excluirProduto(int codigo) {
        String deleteQuery = "DELETE FROM PRODUTO WHERE CODIGO = ?";
        return jdbcTemplate.update(deleteQuery, new Object[] { codigo });
    }

    public void addProduto(Produto produto) {
        String insertQuery = "INSERT INTO PRODUTO(NOME, PRECOUNITARIO, ESTOQUE, ESTOQUEMINIMO, DATACAD) values (?,?,?,?,?)";
        jdbcTemplate.update(insertQuery,
                new Object[] { produto.getNome(), produto.getPrecoUnitario(), produto.getEstoque(),
                        produto.getEstoqueMinimo(), produto.getDateCad() });
    }

    public Produto buscarProduto(Integer codigo) {
        String query = "SELECT * FROM PRODUTO WHERE CODIGO = ?";
        return jdbcTemplate.queryForObject(query, new Object[] { codigo }, new ProdutoRowMapper());
    }

    public List<Produto> buscarProdutosOrdemAlfabetica(String nomeProduto, Boolean emFalta) {
        String query = "";

        if (emFalta) {
            query = "SELECT * FROM PRODUTO WHERE LOWER(NOME) LIKE ? AND ESTOQUE < ESTOQUEMINIMO ORDER BY NOME";
            return jdbcTemplate.query(query, new Object[] { "%" + nomeProduto.toLowerCase() + "%" },
                    new ProdutoRowMapper());
        }

        query = "SELECT * FROM PRODUTO WHERE LOWER(NOME) LIKE ? ORDER BY NOME";
        return jdbcTemplate.query(query, new Object[] { "%" + nomeProduto.toLowerCase() + "%" },
                new ProdutoRowMapper());
    }

    public int atualizarEstoque(Integer codigo, Integer quantidade) {
        String query = "UPDATE PRODUTO SET ESTOQUE = ? WHERE CODIGO = ?";
        return jdbcTemplate.update(query, new Object[] { quantidade, codigo });
    }

}