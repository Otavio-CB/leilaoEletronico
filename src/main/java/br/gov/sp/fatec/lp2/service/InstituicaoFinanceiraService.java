package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.dto.InstituicaoFinanceiraDTO;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Singleton
public class InstituicaoFinanceiraService {

    @Inject
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Inject
    private ModelMapper modelMapper;

    public InstituicaoFinanceiraDTO criarInstituicaoFinanceira(InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        InstituicaoFinanceira instituicao = modelMapper.map(instituicaoFinanceiraDTO, InstituicaoFinanceira.class);
        InstituicaoFinanceira novaInstituicao = instituicaoFinanceiraRepository.save(instituicao);
        return modelMapper.map(novaInstituicao, InstituicaoFinanceiraDTO.class);
    }

    public Optional<InstituicaoFinanceiraDTO> buscarInstituicaoFinanceira(Long id) {
        return instituicaoFinanceiraRepository.findById(id)
                .map(instituicao -> modelMapper.map(instituicao, InstituicaoFinanceiraDTO.class));
    }

    public Optional<InstituicaoFinanceiraDTO> atualizarInstituicaoFinanceira(Long id, InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        return instituicaoFinanceiraRepository.findById(id).map(instituicaoExistente -> {
            InstituicaoFinanceira instituicao = modelMapper.map(instituicaoFinanceiraDTO, InstituicaoFinanceira.class);
            instituicao.setId(id);
            InstituicaoFinanceira atualizado = instituicaoFinanceiraRepository.update(instituicao);
            return modelMapper.map(atualizado, InstituicaoFinanceiraDTO.class);
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
