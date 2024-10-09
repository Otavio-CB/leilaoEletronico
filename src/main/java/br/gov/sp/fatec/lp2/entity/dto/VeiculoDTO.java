package br.gov.sp.fatec.lp2.entity.dto;

import br.gov.sp.fatec.lp2.entity.enums.TipoVeiculo;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link br.gov.sp.fatec.lp2.entity.Veiculo}
 */
@Value
public class VeiculoDTO implements Serializable {
    Long id;
    String descricao;
    Double valorInicial;
    boolean vendido;
    String modelo;
    String marca;
    TipoVeiculo tipo;
}