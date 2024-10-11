package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.TipoVeiculo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "veiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo extends ProdutoLeilao {
    private String modelo;
    private String marca;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;
}
