package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.dto.InstituicaoFinanceiraDTO;
import br.gov.sp.fatec.lp2.mapper.InstituicaoFinanceiraMapper;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class InstituicaoFinanceiraService {

    @Inject
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    public List<InstituicaoFinanceiraDTO> criarInstituicoesFinanceiras(List<InstituicaoFinanceiraDTO> instituicoesFinanceirasDTO) {
        List<InstituicaoFinanceira> instituicoesFinanceiras = instituicoesFinanceirasDTO.stream()
                .map(instituicaoFinanceiraDTO -> InstituicaoFinanceiraMapper.INSTANCE.toEntity(instituicaoFinanceiraDTO))
                .collect(Collectors.toList());
        instituicoesFinanceiras = instituicaoFinanceiraRepository.saveAll(instituicoesFinanceiras);
        return instituicoesFinanceiras.stream()
                .map(instituicao -> InstituicaoFinanceiraMapper.INSTANCE.toDTO(instituicao))
                .collect(Collectors.toList());
    }

    public Optional<InstituicaoFinanceiraDTO> buscarInstituicaoFinanceira(Long id) {
        return instituicaoFinanceiraRepository.findById(id)
                .map(InstituicaoFinanceiraMapper.INSTANCE::toDTO);
    }

    public Optional<InstituicaoFinanceiraDTO> atualizarInstituicaoFinanceira(Long id, InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        return instituicaoFinanceiraRepository.findById(id).map(instituicaoExistente -> {
            InstituicaoFinanceira instituicao = InstituicaoFinanceiraMapper.INSTANCE.toEntity(instituicaoFinanceiraDTO);
            instituicao.setId(id);
            InstituicaoFinanceira atualizado = instituicaoFinanceiraRepository.update(instituicao);
            return InstituicaoFinanceiraMapper.INSTANCE.toDTO(atualizado);
        });
    }

    public boolean removerInstituicaoFinanceira(Long id) {
        Optional<InstituicaoFinanceira> instituicaoOpt = instituicaoFinanceiraRepository.findById(id);
        if (instituicaoOpt.isPresent()) {
            instituicaoFinanceiraRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
