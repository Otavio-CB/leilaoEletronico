package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Lance;
import br.gov.sp.fatec.lp2.entity.dto.LanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LanceMapper {
    LanceMapper INSTANCE = Mappers.getMapper(LanceMapper.class);

    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "leilao.id", target = "leilaoId")
    LanceDTO toDTO(Lance lance);

    @Mapping(source = "clienteId", target = "cliente.id")
    @Mapping(source = "leilaoId", target = "leilao.id")
    Lance toEntity(LanceDTO lanceDTO);

    void toEntity(LanceDTO lanceDTO, @MappingTarget Lance lance);
}
