package br.gov.sp.fatec.lp2.entity.dto;

import br.gov.sp.fatec.lp2.entity.enums.StatusLeilao;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.List;

@Serdeable
@Introspected
@Data
public class LeilaoResumoDTO {
    private Long leilaoId;
    private StatusLeilao status;
    private List<ProdutoVencedorDTO> produtosVencedores;
}
