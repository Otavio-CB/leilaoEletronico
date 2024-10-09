package br.gov.sp.fatec.lp2.entity.dto;

import br.gov.sp.fatec.lp2.entity.enums.TipoDispositivo;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class DispositivoDTO implements Serializable {
    private Long id;
    private String descricao;
    private Double valorInicial;
    private boolean vendido;
    private String nome;
    private TipoDispositivo tipo;
}