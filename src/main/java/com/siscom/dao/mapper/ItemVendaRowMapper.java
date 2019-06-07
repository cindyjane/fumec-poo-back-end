package com.siscom.dao.mapper;

import com.siscom.service.model.ItemVenda;
import com.siscom.service.model.Produto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemVendaRowMapper implements RowMapper<ItemVenda> {

    @Override
    public ItemVenda mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemVenda itemVenda = new ItemVenda();

        itemVenda.setCodProduto(rs.getInt("CODIGO_PRODUTO"));
        itemVenda.setQuantVenda(rs.getInt("QUANTIDADE"));
        itemVenda.setValorVenda(rs.getDouble("VALOR_VENDA"));

        return itemVenda;
    }
}