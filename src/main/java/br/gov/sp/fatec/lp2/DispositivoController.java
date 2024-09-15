package br.gov.sp.fatec.lp2;

import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/dispositivos")
public class DispositivoController {

    @Inject
    DispositivoRepository dispositivoRepository;

    @Post
    public Dispositivo criarDispositivo(@Body Dispositivo dispositivo) {
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
}
