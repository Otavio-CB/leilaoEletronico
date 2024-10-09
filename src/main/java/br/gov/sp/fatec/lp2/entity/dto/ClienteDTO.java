package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class ClienteDTO implements Serializable {
    Long id;
    String nome;
    String email;
    String senha;
}