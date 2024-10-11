package br.gov.sp.fatec.lp2.entity;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.*;

@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class ProdutoLeilao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String descricao;

    @Column
    private Double valorInicial;

    @Column
    private boolean vendido;

    @ManyToOne
    private Leilao leilao;
}
