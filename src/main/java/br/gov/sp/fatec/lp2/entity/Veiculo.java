package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.TipoVeiculo;
import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo extends ProdutoLeilao {

    @Column
    private String modelo;

    @Column
    private String marca;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;
}
