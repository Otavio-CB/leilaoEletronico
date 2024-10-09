package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class LeilaoDTO implements Serializable {
    Long id;
    LocalDateTime dataOcorrencia;
    LocalDateTime dataVisita;
    String endereco;
    String cidade;
    String estado;
}