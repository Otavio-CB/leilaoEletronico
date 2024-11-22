package br.gov.sp.fatec.lp2.entity.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Serdeable
@Introspected
@Data
public class LeilaoDETDTO implements Serializable {
    private Long id;
    private String endereco;
    private String cidade;
    private String estado;
    private String status;
    private List<DispositivoDTO> dispositivos;
    private List<VeiculoDTO> veiculos;
    private List<InstituicaoFinanceiraDTO> instituicoesFinanceiras;
    private List<LanceHistoricoDTO> lancesHistorico;
}
