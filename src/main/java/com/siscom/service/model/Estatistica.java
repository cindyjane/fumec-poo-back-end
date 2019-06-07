package com.siscom.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estatistica implements Serializable {

    private String nome;
    private Integer vezesAcao;
    private Double valorTotal;

    @Override
    public String toString() {
        return "Estatistica{" +
                "nome='" + nome + '\'' +
                ", vezesAcao=" + vezesAcao +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
