package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;


@Data
public class InstituicaoFinanceiraDTO implements Serializable {
    private Long id;
    private String nome;
    private String cnpj;
    private String endereco;
}