package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Value;

import java.io.Serializable;


@Value
public class LanceDTO implements Serializable {
    Long id;
    Double valor;
}