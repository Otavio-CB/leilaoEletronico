package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.dto.DispositivoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DispositivoMapper {
    DispositivoMapper INSTANCE = Mappers.getMapper(DispositivoMapper.class);

    DispositivoDTO toDTO(Dispositivo dispositivo);
    Dispositivo toEntity(DispositivoDTO dispositivoDTO);
    void toEntity(DispositivoDTO dispositivoDTO, @MappingTarget Dispositivo dispositivo);

}
