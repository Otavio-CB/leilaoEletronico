package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.TipoDispositivo;
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
public class Dispositivo extends ProdutoLeilao {

    @Column
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoDispositivo tipo;
}
