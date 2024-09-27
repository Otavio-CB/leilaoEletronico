package br.gov.sp.fatec.lp2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class InstituicaoFinanceira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cnpj;

    private String endereco; // Opcional
}
