package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.repository.DispositivoRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.Leilao;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller("/dispositivos")
public class DispositivoController {

    @Inject
    DispositivoRepository dispositivoRepository;

    @Inject
    LeilaoRepository leilaoRepository;

    @Post
    public Dispositivo criarDispositivo(@Body Dispositivo dispositivo, @PathVariable Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        dispositivo.setLeilao(leilao);
        return dispositivoRepository.save(dispositivo);
    }

    @Get("/{id}")
    public Optional<Dispositivo> buscarDispositivo(@PathVariable Long id) {
        return dispositivoRepository.findById(id);
    }

    @Put("/{id}")
    public Dispositivo atualizarDispositivo(@PathVariable Long id, @Body Dispositivo dispositivo) {
        dispositivo.setId(id);
        return dispositivoRepository.update(dispositivo);
    }

    @Delete("/{id}")
    public void removerDispositivo(@PathVariable Long id) {
        dispositivoRepository.deleteById(id);
    }

    @Put("/{id}/reassociar/{novoLeilaoId}")
    public Dispositivo reassociarDispositivo(@PathVariable Long id, @PathVariable Long novoLeilaoId) {
        Dispositivo dispositivo = dispositivoRepository.findById(id).orElseThrow(() -> new RuntimeException("Dispositivo não encontrado"));

        if (dispositivo.isVendido()) {
            throw new RuntimeException("Não é possível desassociar um dispositivo que já foi vendido.");
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId).orElseThrow(() -> new RuntimeException("Novo leilão não encontrado"));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("O novo leilão deve ocorrer no futuro.");
        }

        dispositivo.setLeilao(novoLeilao);
        return dispositivoRepository.update(dispositivo);
    }
}
