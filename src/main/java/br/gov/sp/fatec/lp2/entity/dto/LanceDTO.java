package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;


@Data
public class LanceDTO implements Serializable {
    private Long id;
    private Double valor;
}