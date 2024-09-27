package br.gov.sp.fatec.lp2;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String tipo;
    private String descricao;
    private Double valorInicial;

    private boolean vendido;

    @ManyToOne
    private Leilao leilao;
}
