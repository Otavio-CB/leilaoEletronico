package br.gov.sp.fatec.lp2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Lance> lances;
}