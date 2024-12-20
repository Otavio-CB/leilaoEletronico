package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LeilaoMapper {
    LeilaoMapper INSTANCE = Mappers.getMapper(LeilaoMapper.class);

    LeilaoDTO toDTO(Leilao leilao);

    Leilao toEntity(LeilaoDTO leilaoDTO);
}
