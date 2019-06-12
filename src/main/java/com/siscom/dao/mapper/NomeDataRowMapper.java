package com.siscom.dao.mapper;

import com.siscom.service.model.NomeData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NomeDataRowMapper implements RowMapper<NomeData> {
    @Override
    public NomeData mapRow(ResultSet rs, int rowNum) throws SQLException {
        NomeData venda = new NomeData();

        venda.setNum(rs.getInt("NUMVENDA"));
        venda.setNome(rs.getString("NOME"));
        venda.setData(rs.getDate("Data"));

        return venda;
    }
}
