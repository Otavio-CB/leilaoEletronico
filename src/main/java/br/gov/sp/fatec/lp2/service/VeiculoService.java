package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.Veiculo;
import br.gov.sp.fatec.lp2.entity.dto.VeiculoDTO;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoOcorridoException;
import br.gov.sp.fatec.lp2.exceptions.veiculo.VeiculoNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.veiculo.VeiculoVendidoException;
import br.gov.sp.fatec.lp2.mapper.VeiculoMapper;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.repository.VeiculoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class VeiculoService {

    @Inject
    private VeiculoRepository veiculoRepository;

    @Inject
    private LeilaoRepository leilaoRepository;

    public List<VeiculoDTO> criarVeiculos(List<VeiculoDTO> veiculosDTO, Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new LeilaoNaoEncontradoException(leilaoId));

        List<Veiculo> veiculos = veiculosDTO.stream()
                .map(veiculoDTO -> {
                    Veiculo veiculo = VeiculoMapper.INSTANCE.toEntity(veiculoDTO);
                    veiculo.setLeilao(leilao);
                    return veiculo;
                })
                .collect(Collectors.toList());

        veiculos = veiculoRepository.saveAll(veiculos);
        return veiculos.stream()
                .map(VeiculoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<VeiculoDTO> buscarVeiculo(Long id) {
        return veiculoRepository.findById(id)
                .map(VeiculoMapper.INSTANCE::toDTO);
    }

    public Optional<VeiculoDTO> atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) {
        return veiculoRepository.findById(id).map(veiculoExistente -> {
            veiculoDTO.setId(id);
            VeiculoMapper.INSTANCE.toEntity(veiculoDTO, veiculoExistente);
            Veiculo atualizado = veiculoRepository.update(veiculoExistente);
            return VeiculoMapper.INSTANCE.toDTO(atualizado);
        });
    }

    public boolean removerVeiculo(Long id) {
        Optional<Veiculo> veiculoOpt = veiculoRepository.findById(id);
        if (veiculoOpt.isPresent()) {
            veiculoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public VeiculoDTO reassociarVeiculo(Long id, Long novoLeilaoId) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new VeiculoNaoEncontradoException(id));

        if (veiculo.isVendido()) {
            throw new VeiculoVendidoException("Não é possível reassociar o veículo com ID " + id + " pois já foi vendido");
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId)
                .orElseThrow(() -> new LeilaoNaoEncontradoException(novoLeilaoId));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            throw new LeilaoOcorridoException();
        }

        veiculo.setLeilao(novoLeilao);
        Veiculo atualizado = veiculoRepository.update(veiculo);
        return VeiculoMapper.INSTANCE.toDTO(atualizado);
    }
}
