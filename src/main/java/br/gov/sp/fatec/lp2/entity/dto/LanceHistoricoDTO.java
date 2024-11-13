package br.gov.sp.fatec.lp2.entity.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.io.Serializable;

@Serdeable
@Introspected
@Data
public class LanceHistoricoDTO implements Serializable {
    private Long lanceId;
    private Double valor;
    private String clienteNome;
    private String produtoDescricao;
    private String produtoTipo;
}
