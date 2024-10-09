package br.gov.sp.fatec.lp2.entity;

import br.gov.sp.fatec.lp2.entity.enums.TipoDispositivo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class Dispositivo extends ProdutoLeilao {
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoDispositivo tipo;
}
