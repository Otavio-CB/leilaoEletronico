package br.gov.sp.fatec.lp2.entity.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Serdeable
@Introspected
@Data
public class InstituicaoFinanceiraDTO implements Serializable {
    private Long id;
    private String nome;
    private String cnpj;
    private String endereco;
}