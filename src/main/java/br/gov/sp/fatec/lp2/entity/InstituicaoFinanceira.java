package br.gov.sp.fatec.lp2.entity;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class InstituicaoFinanceira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column(unique = true)
    private String cnpj;

    @ManyToMany(mappedBy = "instituicoesFinanceiras")
    private List<Leilao> leiloes;
}
