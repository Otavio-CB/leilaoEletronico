package br.gov.sp.fatec.lp2.entity;

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
public class Lance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double valor;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Leilao leilao;
}
