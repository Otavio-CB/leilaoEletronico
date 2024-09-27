package br.gov.sp.fatec.lp2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Lance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valor;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Leilao leilao;
}
