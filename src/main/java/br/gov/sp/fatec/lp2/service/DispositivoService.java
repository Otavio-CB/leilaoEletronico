package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.DispositivoDTO;
import br.gov.sp.fatec.lp2.repository.DispositivoRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Singleton
public class DispositivoService {

    @Inject
    private DispositivoRepository dispositivoRepository;

    @Inject
    private LeilaoRepository leilaoRepository;

    @Inject
    private ModelMapper modelMapper;

    public DispositivoDTO criarDispositivo(DispositivoDTO dispositivoDTO, Long leilaoId) {
        Dispositivo dispositivo = modelMapper.map(dispositivoDTO, Dispositivo.class);
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        dispositivo.setLeilao(leilao);
        dispositivo = dispositivoRepository.save(dispositivo);
        return modelMapper.map(dispositivo, DispositivoDTO.class);
    }

    public Optional<DispositivoDTO> buscarDispositivo(Long id) {
        return dispositivoRepository.findById(id)
                .map(dispositivo -> modelMapper.map(dispositivo, DispositivoDTO.class));
    }

    public Optional<DispositivoDTO> atualizarDispositivo(Long id, DispositivoDTO dispositivoDTO) {
        return dispositivoRepository.findById(id).map(dispositivoExistente -> {
            Dispositivo dispositivo = modelMapper.map(dispositivoDTO, Dispositivo.class);
            dispositivo.setId(id);
            Dispositivo atualizado = dispositivoRepository.update(dispositivo);
            return modelMapper.map(atualizado, DispositivoDTO.class);
        });
    }

    public boolean removerDispositivo(Long id) {
        Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findById(id);
        if (dispositivoOpt.isPresent()) {
            dispositivoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public DispositivoDTO reassociarDispositivo(Long id, Long novoLeilaoId) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado"));

        if (dispositivo.isVendido()) {
            throw new RuntimeException("Dispositivo já foi vendido");
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId)
                .orElseThrow(() -> new RuntimeException("Novo leilão não encontrado"));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("O novo leilão já ocorreu");
        }

        dispositivo.setLeilao(novoLeilao);
        Dispositivo atualizado = dispositivoRepository.update(dispositivo);
        return modelMapper.map(atualizado, DispositivoDTO.class);
    }
}
