package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/instituicoes")
public class InstituicaoFinanceiraController {

    @Inject
    InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Post
    public InstituicaoFinanceira criarInstituicaoFinanceira(@Body InstituicaoFinanceira instituicaoFinanceira) {
        return instituicaoFinanceiraRepository.save(instituicaoFinanceira);
    }

    @Get("/{id}")
    public Optional<InstituicaoFinanceira> buscarInstituicaoFinanceira(@PathVariable Long id) {
        return instituicaoFinanceiraRepository.findById(id);
    }

    @Put("/{id}")
    public InstituicaoFinanceira atualizarInstituicaoFinanceira(@PathVariable Long id, @Body InstituicaoFinanceira instituicaoFinanceira) {
        instituicaoFinanceira.setId(id);
        return instituicaoFinanceiraRepository.update(instituicaoFinanceira);
    }

    @Delete("/{id}")
    public void removerInstituicaoFinanceira(@PathVariable Long id) {
        instituicaoFinanceiraRepository.deleteById(id);
    }
}
