package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.DispositivoDTO;
import br.gov.sp.fatec.lp2.repository.DispositivoRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller("/dispositivos")
@Tag(name = "Dispositivo", description = "Operações relacionadas aos dispositivos em leilões")
public class DispositivoController {

    @Inject
    DispositivoRepository dispositivoRepository;

    @Inject
    LeilaoRepository leilaoRepository;

    @Inject
    ModelMapper modelMapper;

    @Operation(summary = "Cria um novo dispositivo e associa a um leilão")
    @ApiResponse(responseCode = "201", description = "Dispositivo criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Leilão não encontrado ou dados inválidos")
    @Post
    public HttpResponse<DispositivoDTO> criarDispositivo(@Body DispositivoDTO dispositivoDTO, @PathVariable Long leilaoId) {
        Dispositivo dispositivo = modelMapper.map(dispositivoDTO, Dispositivo.class);
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        dispositivo.setLeilao(leilao);
        dispositivo = dispositivoRepository.save(dispositivo);
        DispositivoDTO responseDTO = modelMapper.map(dispositivo, DispositivoDTO.class);
        return HttpResponse.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Busca um dispositivo por ID")
    @ApiResponse(responseCode = "200", description = "Dispositivo encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @Get("/{id}")
    public HttpResponse<DispositivoDTO> buscarDispositivo(@PathVariable Long id) {
        return dispositivoRepository.findById(id)
                .map(dispositivo -> HttpResponse.ok(modelMapper.map(dispositivo, DispositivoDTO.class)))
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um dispositivo existente")
    @ApiResponse(responseCode = "200", description = "Dispositivo atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @Put("/{id}")
    public HttpResponse<DispositivoDTO> atualizarDispositivo(@PathVariable Long id, @Body DispositivoDTO dispositivoDTO) {
        Optional<Dispositivo> optionalDispositivo = dispositivoRepository.findById(id);
        if (optionalDispositivo.isPresent()) {
            Dispositivo dispositivo = modelMapper.map(dispositivoDTO, Dispositivo.class);
            dispositivo.setId(id);
            dispositivo = dispositivoRepository.update(dispositivo);
            return HttpResponse.ok(modelMapper.map(dispositivo, DispositivoDTO.class));
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Remove um dispositivo por ID")
    @ApiResponse(responseCode = "204", description = "Dispositivo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @Delete("/{id}")
    public HttpResponse<Void> removerDispositivo(@PathVariable Long id) {
        Optional<Dispositivo> optionalDispositivo = dispositivoRepository.findById(id);
        if (optionalDispositivo.isPresent()) {
            dispositivoRepository.deleteById(id);
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reassocia um dispositivo a um novo leilão")
    @ApiResponse(responseCode = "200", description = "Dispositivo reassociado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Dispositivo.class)))
    @ApiResponse(responseCode = "400", description = "Erro de validação no leilão ou dispositivo")
    @ApiResponse(responseCode = "404", description = "Dispositivo ou leilão não encontrado")
    @Put("/{id}/reassociar/{novoLeilaoId}")
    public HttpResponse<Dispositivo> reassociarDispositivo(@PathVariable Long id, @PathVariable Long novoLeilaoId) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado"));

        if (dispositivo.isVendido()) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId)
                .orElseThrow(() -> new RuntimeException("Novo leilão não encontrado"));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(null);
        }

        dispositivo.setLeilao(novoLeilao);
        return HttpResponse.ok(dispositivoRepository.update(dispositivo));
    }

}
