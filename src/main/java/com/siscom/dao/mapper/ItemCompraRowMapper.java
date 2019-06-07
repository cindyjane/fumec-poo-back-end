package com.siscom.dao.mapper;

import com.siscom.service.model.ItemCompra;
import com.siscom.service.model.Produto;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemCompraRowMapper implements RowMapper<ItemCompra> {

    @Override
    public ItemCompra mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemCompra itemCompra = new ItemCompra();

        itemCompra.setCodProduto(rs.getInt("CODIGO_PRODUTO"));
        itemCompra.setQuantCompra(rs.getInt("QUANTIDADE"));
        itemCompra.setValorUnitario(rs.getDouble("VALOR_UNITARIO"));


        return itemCompra;
    }

}