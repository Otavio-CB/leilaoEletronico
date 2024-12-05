package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.DispositivoDTO;
import br.gov.sp.fatec.lp2.exceptions.dispositivo.DispositivoNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.dispositivo.DispositivoVendidoException;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoOcorridoException;
import br.gov.sp.fatec.lp2.mapper.DispositivoMapper;
import br.gov.sp.fatec.lp2.repository.DispositivoRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class DispositivoService {

    @Inject
    private DispositivoRepository dispositivoRepository;

    @Inject
    private LeilaoRepository leilaoRepository;

    public List<DispositivoDTO> criarDispositivos(List<DispositivoDTO> dispositivosDTO, Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new LeilaoNaoEncontradoException(leilaoId));

        List<Dispositivo> dispositivos = dispositivosDTO.stream()
                .map(dispositivoDTO -> {
                    Dispositivo dispositivo = DispositivoMapper.INSTANCE.toEntity(dispositivoDTO);
                    dispositivo.setLeilao(leilao);
                    return dispositivo;
                })
                .collect(Collectors.toList());

        dispositivos = dispositivoRepository.saveAll(dispositivos);
        return dispositivos.stream()
                .map(dispositivo -> DispositivoMapper.INSTANCE.toDTO(dispositivo))
                .collect(Collectors.toList());
    }

    public Optional<DispositivoDTO> buscarDispositivo(Long id) {
        return Optional.ofNullable(dispositivoRepository.findById(id)
                .map(DispositivoMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new DispositivoNaoEncontradoException(id)));
    }

    public Optional<DispositivoDTO> atualizarDispositivo(Long id, DispositivoDTO dispositivoDTO) {
        return Optional.ofNullable(dispositivoRepository.findById(id).map(dispositivoExistente -> {
            dispositivoDTO.setId(id);
            DispositivoMapper.INSTANCE.toEntity(dispositivoDTO, dispositivoExistente);
            Dispositivo atualizado = dispositivoRepository.update(dispositivoExistente);
            return DispositivoMapper.INSTANCE.toDTO(atualizado);
        }).orElseThrow(() -> new DispositivoNaoEncontradoException(id)));
    }

    public boolean removerDispositivo(Long id) {
        Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findById(id);
        if (dispositivoOpt.isPresent()) {
            dispositivoRepository.deleteById(id);
            return true;
        }
        throw new DispositivoNaoEncontradoException(id);
    }

    public DispositivoDTO reassociarDispositivo(Long id, Long novoLeilaoId) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new DispositivoNaoEncontradoException(id));

        if (dispositivo.isVendido()) {
            throw new DispositivoVendidoException();
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId)
                .orElseThrow(() -> new LeilaoNaoEncontradoException(novoLeilaoId));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            throw new LeilaoOcorridoException();
        }

        dispositivo.setLeilao(novoLeilao);
        Dispositivo atualizado = dispositivoRepository.update(dispositivo);
        return DispositivoMapper.INSTANCE.toDTO(atualizado);
    }
}
