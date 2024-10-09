package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.DispositivoDTO;
import br.gov.sp.fatec.lp2.repository.DispositivoRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Controller("/dispositivos")
public class DispositivoController {

    @Inject
    DispositivoRepository dispositivoRepository;

    @Inject
    LeilaoRepository leilaoRepository;

    @Inject
    ModelMapper modelMapper;

    @Post
    public DispositivoDTO criarDispositivo(@Body DispositivoDTO dispositivoDTO, @PathVariable Long leilaoId) {
        Dispositivo dispositivo = modelMapper.map(dispositivoDTO, Dispositivo.class);
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        dispositivo.setLeilao(leilao);
        dispositivo = dispositivoRepository.save(dispositivo);
        return modelMapper.map(dispositivo, DispositivoDTO.class);
    }

    @Get("/{id}")
    public Optional<DispositivoDTO> buscarDispositivo(@PathVariable Long id) {
        return dispositivoRepository.findById(id)
                .map(dispositivo -> modelMapper.map(dispositivo, DispositivoDTO.class));
    }

    @Put("/{id}")
    public DispositivoDTO atualizarDispositivo(@PathVariable Long id, @Body DispositivoDTO dispositivoDTO) {
        Dispositivo dispositivo = modelMapper.map(dispositivoDTO, Dispositivo.class);
        dispositivo.setId(id);
        dispositivo = dispositivoRepository.update(dispositivo);
        return modelMapper.map(dispositivo, DispositivoDTO.class);
    }

    @Delete("/{id}")
    public void removerDispositivo(@PathVariable Long id) {
        dispositivoRepository.deleteById(id);
    }
}
