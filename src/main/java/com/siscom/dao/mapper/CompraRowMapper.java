package com.siscom.dao.mapper;

import com.siscom.service.model.Compra;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraRowMapper implements RowMapper<Compra> {

    @Override
    public Compra mapRow(ResultSet rs, int rowNum) throws SQLException {
        Compra compra = new Compra();

        compra.setDataCompra(rs.getDate("DATA"));
        compra.setCodFornecedor(rs.getInt("COD_FORNECEDOR"));
        compra.setNumCompra(rs.getInt("NUMCOMPRA"));
       // compra.setCompraItens(rs.getString(""));


        return compra;
    }


}
