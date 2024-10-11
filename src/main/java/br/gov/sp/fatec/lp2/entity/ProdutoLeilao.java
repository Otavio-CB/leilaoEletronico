package br.gov.sp.fatec.lp2.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class ProdutoLeilao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Double valorInicial;
    private boolean vendido;

    @ManyToOne
    private Leilao leilao;
}
