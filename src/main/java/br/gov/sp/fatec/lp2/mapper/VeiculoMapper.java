package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Veiculo;
import br.gov.sp.fatec.lp2.entity.dto.VeiculoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VeiculoMapper {
    VeiculoMapper INSTANCE = Mappers.getMapper(VeiculoMapper.class);

    VeiculoDTO toDTO(Veiculo veiculo);
    Veiculo toEntity(VeiculoDTO veiculoDTO);
    void updateEntityFromDto(VeiculoDTO veiculoDTO, @MappingTarget Veiculo veiculo);
}
