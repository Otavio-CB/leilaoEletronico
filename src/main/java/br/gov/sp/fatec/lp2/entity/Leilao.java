package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.StatusLeilao;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Serdeable
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

    @Column(name = "status_leilao")
    @Enumerated(EnumType.STRING)
    private StatusLeilao status;

    @OneToMany(mappedBy = "leilao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Dispositivo> dispositivos = new ArrayList<>();

    @OneToMany(mappedBy = "leilao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Veiculo> veiculos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "leilao_instituicao_financeira", joinColumns = @JoinColumn(name = "leilao_id"), inverseJoinColumns = @JoinColumn(name = "instituicao_financeira_id"))
    private List<InstituicaoFinanceira> instituicoesFinanceiras = new ArrayList<>(); // Inicializando a lista

}
