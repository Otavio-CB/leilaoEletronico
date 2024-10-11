package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.dto.ClienteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDTO toDTO(Cliente cliente);

    Cliente toEntity(ClienteDTO clienteDTO);
}
