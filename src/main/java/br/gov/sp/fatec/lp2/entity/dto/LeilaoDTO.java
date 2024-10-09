package br.gov.sp.fatec.lp2.entity.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LeilaoDTO implements Serializable {
    private Long id;
    private LocalDateTime dataOcorrencia;
    private LocalDateTime dataVisita;
    private String endereco;
    private String cidade;
    private String estado;
    private List<Long> instituicaoFinanceiraIds;
}