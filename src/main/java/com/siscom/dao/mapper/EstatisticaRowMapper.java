package com.siscom.dao.mapper;

import com.siscom.service.model.Cliente;
import com.siscom.service.model.Estatistica;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EstatisticaRowMapper implements RowMapper<Estatistica> {

    @Override
    public Estatistica mapRow(ResultSet rs, int rowNum) throws SQLException {
        Estatistica estatistica = new Estatistica();

        estatistica.setNome(rs.getString("NOME"));
        estatistica.setVezesAcao(rs.getInt("QTD_ACAO"));
        estatistica.setValorTotal(rs.getDouble("VALOR_TOTAL"));

        return estatistica;
    }



}