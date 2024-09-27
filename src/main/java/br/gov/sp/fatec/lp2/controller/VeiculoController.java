package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.repository.VeiculoRepository;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.Veiculo;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller("/veiculos")
public class VeiculoController {

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    LeilaoRepository leilaoRepository;

    @Post
    public Veiculo criarVeiculo(@Body Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    @Get("/{id}")
    public Optional<Veiculo> buscarVeiculo(@PathVariable Long id) {
        return veiculoRepository.findById(id);
    }

    @Put("/{id}")
    public Veiculo atualizarVeiculo(@PathVariable Long id, @Body Veiculo veiculo) {
        veiculo.setId(id);
        return veiculoRepository.update(veiculo);
    }

    @Delete("/{id}")
    public void removerVeiculo(@PathVariable Long id) {
        veiculoRepository.deleteById(id);
    }

    @Put("/{id}/reassociar/{novoLeilaoId}")
    public Veiculo reassociarVeiculo(@PathVariable Long id, @PathVariable Long novoLeilaoId) {
        Veiculo veiculo = veiculoRepository.findById(id).orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (veiculo.isVendido()) {
            throw new RuntimeException("Não é possível desassociar um veículo que já foi vendido.");
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId).orElseThrow(() -> new RuntimeException("Novo leilão não encontrado"));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("O novo leilão deve ocorrer no futuro.");
        }

        veiculo.setLeilao(novoLeilao);
        return veiculoRepository.update(veiculo);
    }
}
