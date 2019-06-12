package com.siscom.dao.mapper;

import com.siscom.service.model.NomeData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraRowMapper implements RowMapper<NomeData> {

    @Override
    public NomeData mapRow(ResultSet rs, int rowNum) throws SQLException {
        NomeData compra = new NomeData();

        compra.setNum(rs.getInt("NUMCOMPRA"));
        compra.setNome(rs.getString("NOME"));
        compra.setData(rs.getDate("Data"));

        return compra;
    }


}
