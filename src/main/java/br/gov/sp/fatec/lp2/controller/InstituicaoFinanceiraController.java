package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.dto.InstituicaoFinanceiraDTO;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Controller("/instituicoes")
public class InstituicaoFinanceiraController {

    @Inject
    InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Inject
    private ModelMapper modelMapper;

    @Post
    public InstituicaoFinanceiraDTO criarInstituicaoFinanceira(@Body InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        InstituicaoFinanceira instituicaoFinanceira = modelMapper.map(instituicaoFinanceiraDTO, InstituicaoFinanceira.class);
        return modelMapper.map(instituicaoFinanceiraRepository.save(instituicaoFinanceira), InstituicaoFinanceiraDTO.class);
    }

    @Get("/{id}")
    public Optional<InstituicaoFinanceiraDTO> buscarInstituicaoFinanceira(@PathVariable Long id) {
        return instituicaoFinanceiraRepository.findById(id)
                .map(instituicao -> modelMapper.map(instituicao, InstituicaoFinanceiraDTO.class));
    }

    @Put("/{id}")
    public InstituicaoFinanceiraDTO atualizarInstituicaoFinanceira(@PathVariable Long id, @Body InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        InstituicaoFinanceira instituicao = modelMapper.map(instituicaoFinanceiraDTO, InstituicaoFinanceira.class);
        instituicao.setId(id);
        return modelMapper.map(instituicaoFinanceiraRepository.update(instituicao), InstituicaoFinanceiraDTO.class);
    }


    @Delete("/{id}")
    public void removerInstituicaoFinanceira(@PathVariable Long id) {
        instituicaoFinanceiraRepository.deleteById(id);
    }
}
