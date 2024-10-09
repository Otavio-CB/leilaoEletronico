package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Value;

import java.io.Serializable;


@Value
public class InstituicaoFinanceiraDTO implements Serializable {
    Long id;
    String nome;
    String cnpj;
    String endereco;
}