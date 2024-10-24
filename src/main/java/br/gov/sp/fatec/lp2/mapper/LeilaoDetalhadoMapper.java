package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDetalhadoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface LeilaoDetalhadoMapper {
    LeilaoDetalhadoMapper INSTANCE = Mappers.getMapper(LeilaoDetalhadoMapper.class);

    @Mapping(target = "produtos", source = "dispositivos")
    @Mapping(target = "veiculos", source = "veiculos")
    @Mapping(target = "totalProdutos", expression = "java(leilao.getDispositivos().size() + leilao.getVeiculos().size())")
    LeilaoDetalhadoDTO toDTO(Leilao leilao);
}

