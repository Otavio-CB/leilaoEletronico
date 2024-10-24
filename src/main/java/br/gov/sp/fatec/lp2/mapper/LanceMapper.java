package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Lance;
import br.gov.sp.fatec.lp2.entity.dto.LanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LanceMapper {

    LanceMapper INSTANCE = Mappers.getMapper(LanceMapper.class);

    @Mapping(source = "veiculo.id", target = "veiculoId")
    @Mapping(source = "dispositivo.id", target = "dispositivoId")
    LanceDTO toDTO(Lance lance);

    @Mapping(source = "veiculoId", target = "veiculo.id")
    @Mapping(source = "dispositivoId", target = "dispositivo.id")
    Lance toEntity(LanceDTO lanceDTO);
}

