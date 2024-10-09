package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.Veiculo;
import br.gov.sp.fatec.lp2.entity.dto.VeiculoDTO;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.repository.VeiculoRepository;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller("/veiculos")
public class VeiculoController {

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    LeilaoRepository leilaoRepository;

    @Inject
    private ModelMapper modelMapper;

    @Post
    public VeiculoDTO criarVeiculo(@Body VeiculoDTO veiculoDTO, @PathVariable Long leilaoId) {
        Veiculo veiculo = modelMapper.map(veiculoDTO, Veiculo.class);
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        veiculo.setLeilao(leilao);
        return modelMapper.map(veiculoRepository.save(veiculo), VeiculoDTO.class);
    }

    @Get("/{id}")
    public Optional<VeiculoDTO> buscarVeiculo(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class));
    }

    @Put("/{id}")
    public VeiculoDTO atualizarVeiculo(@PathVariable Long id, @Body VeiculoDTO veiculoDTO) {
        Veiculo veiculo = modelMapper.map(veiculoDTO, Veiculo.class);
        veiculo.setId(id);
        return modelMapper.map(veiculoRepository.update(veiculo), VeiculoDTO.class);
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
