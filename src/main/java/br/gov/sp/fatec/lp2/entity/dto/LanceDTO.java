package br.gov.sp.fatec.lp2.entity.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.io.Serializable;

@Serdeable
@Introspected
@Data
public class LanceDTO implements Serializable {
    private Long id;
    private Double valor;
    private Long clienteId;
    private Long leilaoId;
}
