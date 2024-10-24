package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.*;
import br.gov.sp.fatec.lp2.mapper.LanceMapper;
import br.gov.sp.fatec.lp2.repository.*;
import br.gov.sp.fatec.lp2.entity.dto.LanceDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Optional;

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

        // Associar o cliente
        Cliente cliente = clienteRepository.findById(lanceDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        lance.setCliente(cliente);

        // Verificar se pelo menos um produto (veiculo ou dispositivo) está presente
        if (lanceDTO.getVeiculoId() == null && lanceDTO.getDispositivoId() == null) {
            throw new IllegalArgumentException("É necessário associar um Veículo ou Dispositivo ao lance");
        }

        // Verificar e carregar o Veiculo, se fornecido
        if (lanceDTO.getVeiculoId() != null) {
            Veiculo veiculo = veiculoRepository.findById(lanceDTO.getVeiculoId())
                    .orElseThrow(() -> new IllegalArgumentException("Veiculo não encontrado"));
            lance.setVeiculo(veiculo);  // O Veículo já está salvo e gerenciado pelo JPA
        }

        // Verificar e carregar o Dispositivo, se fornecido
        if (lanceDTO.getDispositivoId() != null) {
            Dispositivo dispositivo = dispositivoRepository.findById(lanceDTO.getDispositivoId())
                    .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado"));
            lance.setDispositivo(dispositivo);  // O Dispositivo já está salvo e gerenciado pelo JPA
        }

        // Salvar o lance
        lance = lanceRepository.save(lance);
        return LanceMapper.INSTANCE.toDTO(lance);
    }




    public Optional<LanceDTO> buscarLance(Long id) {
        return lanceRepository.findById(id)
                .map(LanceMapper.INSTANCE::toDTO);
    }
}
