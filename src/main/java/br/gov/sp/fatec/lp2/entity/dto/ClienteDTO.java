package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class ClienteDTO implements Serializable {
    private Long id;
    private String nome;
    private String email;
    private String senha;
}