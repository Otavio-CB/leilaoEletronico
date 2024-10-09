package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.TipoVeiculo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class Veiculo extends ProdutoLeilao {
    private String modelo;
    private String marca;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;
}
