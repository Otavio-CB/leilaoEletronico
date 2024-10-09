package br.gov.sp.fatec.lp2.entity.dto;

import br.gov.sp.fatec.lp2.entity.enums.TipoDispositivo;
import lombok.Value;

import java.io.Serializable;

@Value
public class DispositivoDTO implements Serializable {
    Long id;
    String descricao;
    Double valorInicial;
    boolean vendido;
    String nome;
    TipoDispositivo tipo;
}