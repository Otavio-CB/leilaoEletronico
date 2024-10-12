package br.gov.sp.fatec.lp2.entity;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="leilao")
public class Leilao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dataOcorrencia;

    @Column
    private LocalDateTime dataVisita;

    @Column
    private String endereco;

    @Column
    private String cidade;

    @Column
    private String estado;

    @OneToMany
    private List<Dispositivo> dispositivos;

    @OneToMany
    private List<Veiculo> veiculos;

    @ManyToMany
    @JoinTable(name = "leilao_instituicao_financeira", joinColumns = @JoinColumn(name = "leilao_id"), inverseJoinColumns = @JoinColumn(name = "instituicao_financeira_id"))
    private List<InstituicaoFinanceira> instituicoesFinanceiras;
}
