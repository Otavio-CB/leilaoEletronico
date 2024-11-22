package br.gov.sp.fatec.lp2.mapper;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDETDTO;
import br.gov.sp.fatec.lp2.entity.dto.LanceHistoricoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface LeilaoDETDTOMapper {

    LeilaoDETDTOMapper INSTANCE = Mappers.getMapper(LeilaoDETDTOMapper.class);

    @Mapping(target = "dispositivos", source = "dispositivos")
    @Mapping(target = "veiculos", source = "veiculos")
    @Mapping(target = "instituicoesFinanceiras", source = "instituicoesFinanceiras")
    @Mapping(target = "lancesHistorico", expression = "java(mapLancesToHistorico(leilao))")
    LeilaoDETDTO toLeilaoDETDTO(Leilao leilao);

    default List<LanceHistoricoDTO> mapLancesToHistorico(Leilao leilao) {
        List<LanceHistoricoDTO> lancesHistorico = new ArrayList<>();

        if (leilao.getDispositivos() != null) {
            leilao.getDispositivos().forEach(dispositivo -> {
                dispositivo.getLances().forEach(lance -> {
                    LanceHistoricoDTO dto = new LanceHistoricoDTO();
                    dto.setLanceId(lance.getId());
                    dto.setValor(lance.getValor());
                    dto.setClienteNome(lance.getCliente().getNome());
                    dto.setProdutoDescricao(dispositivo.getDescricao());
                    dto.setProdutoTipo("Dispositivo");
                    lancesHistorico.add(dto);
                });
            });
        }

        if (leilao.getVeiculos() != null) {
            leilao.getVeiculos().forEach(veiculo -> {
                veiculo.getLances().forEach(lance -> {
                    LanceHistoricoDTO dto = new LanceHistoricoDTO();
                    dto.setLanceId(lance.getId());
                    dto.setValor(lance.getValor());
                    dto.setClienteNome(lance.getCliente().getNome());
                    dto.setProdutoDescricao(veiculo.getDescricao());
                    dto.setProdutoTipo("Veículo");
                    lancesHistorico.add(dto);
                });
            });
        }

        return lancesHistorico;
    }
}
