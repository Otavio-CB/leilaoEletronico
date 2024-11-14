package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.Lance;
import br.gov.sp.fatec.lp2.entity.Veiculo;
import br.gov.sp.fatec.lp2.entity.dto.LanceDTO;
import br.gov.sp.fatec.lp2.entity.dto.LanceHistoricoDTO;
import br.gov.sp.fatec.lp2.mapper.LanceMapper;
import br.gov.sp.fatec.lp2.repository.ClienteRepository;
import br.gov.sp.fatec.lp2.repository.DispositivoRepository;
import br.gov.sp.fatec.lp2.repository.LanceRepository;
import br.gov.sp.fatec.lp2.repository.VeiculoRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class LanceService {

    @Inject
    private LanceRepository lanceRepository;

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private VeiculoRepository veiculoRepository;

    @Inject
    private DispositivoRepository dispositivoRepository;

    public LanceDTO criarLance(LanceDTO lanceDTO) {
        Lance lance = LanceMapper.INSTANCE.toEntity(lanceDTO);

        lance.setDataHora(LocalDateTime.now());

        Cliente cliente = clienteRepository.findById(lanceDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        lance.setCliente(cliente);

        if (lanceDTO.getVeiculoId() == null && lanceDTO.getDispositivoId() == null) {
            throw new IllegalArgumentException("É necessário associar um Veículo ou Dispositivo ao lance");
        }

        if (lanceDTO.getVeiculoId() != null) {
            Veiculo veiculo = veiculoRepository.findById(lanceDTO.getVeiculoId())
                    .orElseThrow(() -> new IllegalArgumentException("Veiculo não encontrado"));
            lance.setVeiculo(veiculo);  // O Veículo já está salvo e gerenciado pelo JPA
        }

        if (lanceDTO.getDispositivoId() != null) {
            Dispositivo dispositivo = dispositivoRepository.findById(lanceDTO.getDispositivoId())
                    .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado"));
            lance.setDispositivo(dispositivo);  // O Dispositivo já está salvo e gerenciado pelo JPA
        }

        lance = lanceRepository.save(lance);
        return LanceMapper.INSTANCE.toDTO(lance);
    }

    @Transactional(readOnly = true)
    public List<LanceHistoricoDTO> buscarHistoricoDeLances(Long produtoId, String tipoProduto) {
        List<Lance> lances;

        if (tipoProduto.equalsIgnoreCase("DISPOSITIVO")) {
            lances = lanceRepository.findByDispositivoId(produtoId);
        } else if (tipoProduto.equalsIgnoreCase("VEICULO")) {
            lances = lanceRepository.findByVeiculoId(produtoId);
        } else {
            throw new IllegalArgumentException("Tipo de produto inválido");
        }

        return lances.stream()
                .sorted((l1, l2) -> l1.getId().compareTo(l2.getId()))
                .map(lance -> {
                    LanceHistoricoDTO dto = new LanceHistoricoDTO();
                    dto.setLanceId(lance.getId());
                    dto.setValor(lance.getValor());
                    dto.setClienteNome(lance.getCliente().getNome());
                    dto.setProdutoDescricao(
                            tipoProduto.equalsIgnoreCase("DISPOSITIVO") ?
                                    lance.getDispositivo().getNome() :
                                    lance.getVeiculo().getModelo());
                    dto.setProdutoTipo(tipoProduto.toUpperCase());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<LanceDTO> buscarLance(Long id) {
        return lanceRepository.findById(id)
                .map(LanceMapper.INSTANCE::toDTO);
    }
}
