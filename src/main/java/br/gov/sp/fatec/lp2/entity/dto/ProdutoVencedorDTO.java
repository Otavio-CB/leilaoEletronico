package br.gov.sp.fatec.lp2.entity.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Serdeable
@Introspected
public class ProdutoVencedorDTO {
    private Long produtoId;
    private String produtoDescricao;
    private Double valorVencedor;
    private String clienteNome;
    private String tipoProduto; // "DISPOSITIVO" ou "VEICULO"
}
