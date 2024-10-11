package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.TipoDispositivo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dispositivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dispositivo extends ProdutoLeilao {
    private Integer id;
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoDispositivo tipo;
}
