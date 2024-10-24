package br.gov.sp.fatec.lp2.entity.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Serdeable
@Introspected
@Data
public class LeilaoDetalhadoDTO {
    private Long id;
    private LocalDateTime dataOcorrencia;
    private LocalDateTime dataVisita;
    private String endereco;
    private String cidade;
    private String estado;
    private List<DispositivoDTO> produtos;
    private List<VeiculoDTO> veiculos;
    private List<InstituicaoFinanceiraDTO> instituicoesFinanceiras;
    private int totalProdutos;
}
