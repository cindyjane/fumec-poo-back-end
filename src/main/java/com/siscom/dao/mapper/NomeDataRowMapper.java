package com.siscom.dao.mapper;

import com.siscom.service.model.NomeData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NomeDataRowMapper implements RowMapper<NomeData> {
    @Override
    public NomeData mapRow(ResultSet rs, int rowNum) throws SQLException {
        NomeData compra = new NomeData();

        compra.setNome(rs.getString("NOME"));
        compra.setData(rs.getDate("Data"));

        return compra;
    }
}
