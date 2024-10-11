package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.Veiculo;
import br.gov.sp.fatec.lp2.entity.dto.VeiculoDTO;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.repository.VeiculoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Singleton
public class VeiculoService {

    @Inject
    private VeiculoRepository veiculoRepository;

    @Inject
    private LeilaoRepository leilaoRepository;

    @Inject
    private ModelMapper modelMapper;

    public VeiculoDTO criarVeiculo(VeiculoDTO veiculoDTO, Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        Veiculo veiculo = modelMapper.map(veiculoDTO, Veiculo.class);
        veiculo.setLeilao(leilao);
        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);
        return modelMapper.map(veiculoSalvo, VeiculoDTO.class);
    }

    public Optional<VeiculoDTO> buscarVeiculo(Long id) {
        return veiculoRepository.findById(id)
                .map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class));
    }

    public VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) {
        veiculoRepository.findById(id).orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        Veiculo veiculo = modelMapper.map(veiculoDTO, Veiculo.class);
        veiculo.setId(id);
        Veiculo atualizado = veiculoRepository.update(veiculo);
        return modelMapper.map(atualizado, VeiculoDTO.class);
    }

    public boolean removerVeiculo(Long id) {
        Optional<Veiculo> veiculoOpt = veiculoRepository.findById(id);
        if (veiculoOpt.isPresent()) {
            veiculoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Veiculo reassociarVeiculo(Long veiculoId, Long novoLeilaoId) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (veiculo.isVendido()) {
            throw new RuntimeException("Não é possível reassociar um veículo vendido");
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId)
                .orElseThrow(() -> new RuntimeException("Novo leilão não encontrado"));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("O novo leilão já ocorreu");
        }

        veiculo.setLeilao(novoLeilao);
        return veiculoRepository.update(veiculo);
    }
}
