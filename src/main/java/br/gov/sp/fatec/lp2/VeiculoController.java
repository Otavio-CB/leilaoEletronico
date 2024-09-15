package br.gov.sp.fatec.lp2;

import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import java.util.Optional;

@Controller("/veiculos")
public class VeiculoController {

    @Inject
    VeiculoRepository veiculoRepository;

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
}
