package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.entity.Leilao;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/leiloes")
public class LeilaoController {

    @Inject
    LeilaoRepository leilaoRepository;

    @Post
    public Leilao criarLeilao(@Body Leilao leilao) {
        return leilaoRepository.save(leilao);
    }

    @Get("/{id}")
    public Optional<Leilao> buscarLeilao(@PathVariable Long id) {
        return leilaoRepository.findById(id);
    }

    @Put("/{id}")
    public Leilao atualizarLeilao(@PathVariable Long id, @Body Leilao leilao) {
        leilao.setId(id);
        return leilaoRepository.update(leilao);
    }

    @Delete("/{id}")
    public void removerLeilao(@PathVariable Long id) {
        leilaoRepository.deleteById(id);
    }
}
