package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.dto.InstituicaoFinanceiraDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstituicaoFinanceiraMapper {
    InstituicaoFinanceiraMapper INSTANCE = Mappers.getMapper(InstituicaoFinanceiraMapper.class);

    InstituicaoFinanceiraDTO toDTO(InstituicaoFinanceira instituicaoFinanceira);

    InstituicaoFinanceira toEntity(InstituicaoFinanceiraDTO instituicaoFinanceiraDTO);
}
