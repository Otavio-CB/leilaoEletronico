package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Lance;
import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.mapper.LanceMapper;
import br.gov.sp.fatec.lp2.repository.ClienteRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.repository.LanceRepository;
import br.gov.sp.fatec.lp2.entity.dto.LanceDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class LanceService {

    @Inject
    private LanceRepository lanceRepository;

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private LeilaoRepository leilaoRepository;

    @Inject
    private LanceMapper lanceMapper;

    public LanceDTO salvarLance(LanceDTO lanceDTO) {
        Cliente cliente = clienteRepository.findById(lanceDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        Leilao leilao = leilaoRepository.findById(lanceDTO.getLeilaoId())
                .orElseThrow(() -> new IllegalArgumentException("Leilão não encontrado"));

        Lance lance = lanceMapper.toEntity(lanceDTO);
        lance.setCliente(cliente);
        lance.setLeilao(leilao);

        Lance salvo = lanceRepository.save(lance);
        return lanceMapper.toDTO(salvo);
    }

    public Iterable<LanceDTO> buscarTodos() {
        return lanceRepository.findAll().stream().map(lanceMapper::toDTO).toList();
    }

    public LanceDTO atualizarLance(Long id, LanceDTO lanceDTO) {
        Lance lance = lanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lance não encontrado"));
        lanceMapper.toEntity(lanceDTO, lance);
        return lanceMapper.toDTO(lanceRepository.update(lance));
    }

    public void removerLance(Long id) {
        lanceRepository.deleteById(id);
    }
}
