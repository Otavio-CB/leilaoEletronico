package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.TipoVeiculo;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Serdeable
@Entity
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="veiculo")
public class Veiculo extends ProdutoLeilao {

    @Column
    private String modelo;

    @Column
    private String marca;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL)
    private List<Lance> lances;
}
