package br.gov.sp.fatec.lp2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String modelo;
    private String marca;
    private String tipo;
    private Double valorInicial;

    private boolean vendido;

    @ManyToOne
    private Leilao leilao;
}
