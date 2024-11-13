package br.gov.sp.fatec.lp2.entity.dto;

import br.gov.sp.fatec.lp2.entity.enums.TipoVeiculo;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.io.Serializable;

@Serdeable
@Introspected
@Data
public class VeiculoDTO implements Serializable {
    private Long id;
    private String descricao;
    private Double valorInicial;
    private boolean vendido;
    private String modelo;
    private String marca;
    private TipoVeiculo tipo;
}