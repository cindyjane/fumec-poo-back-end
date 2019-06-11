package com.siscom.controller.dto;

import com.siscom.service.model.ItemCompra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraDto {
    private Integer codigoFornecedor;
    private ArrayList<ItemCompra> itensCompra;
}
