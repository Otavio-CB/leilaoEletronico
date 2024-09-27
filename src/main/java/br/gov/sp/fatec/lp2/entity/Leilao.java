package br.gov.sp.fatec.lp2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Leilao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataOcorrencia;
    private LocalDateTime dataVisita;
    private String endereco;
    private String cidade;
    private String estado;

    @OneToMany
    private List<Dispositivo> dispositivos;

    @OneToMany
    private List<Veiculo> veiculos;

    @ManyToMany
    @JoinTable(name = "leilao_instituicao_financeira", joinColumns = @JoinColumn(name = "leilao_id"), inverseJoinColumns = @JoinColumn(name = "instituicao_financeira_id"))
    private List<InstituicaoFinanceira> instituicoesFinanceiras;
}
