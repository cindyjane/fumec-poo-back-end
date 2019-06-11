package com.siscom.dao.mapper;

import com.siscom.service.model.Vendedor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VendedorRowMapper implements RowMapper<Vendedor> {

    @Override
    public Vendedor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vendedor vendedor = new Vendedor();

        vendedor.setCodigo(rs.getInt("CODIGO"));
        vendedor.setNome(rs.getString("NOME"));
        vendedor.setTelefones(rs.getString("TELEFONES"));
        vendedor.setEmail(rs.getString("EMAIL"));
        vendedor.setDateCad(rs.getDate("DATECAD"));
        vendedor.setCpf(rs.getString("VEN_CPF"));
        vendedor.setMetaMensal(rs.getDouble("VEN_METAMENSAL"));

        return vendedor;
    }
}