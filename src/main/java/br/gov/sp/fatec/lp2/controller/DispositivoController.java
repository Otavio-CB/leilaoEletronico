package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.dto.DispositivoDTO;
import br.gov.sp.fatec.lp2.service.DispositivoService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import java.util.List;


@Controller("/dispositivos")
@Tag(name = "Dispositivo", description = "Operações relacionadas aos dispositivos em leilões")
public class DispositivoController {

    @Inject
    private DispositivoService dispositivoService;

    @Operation(summary = "Cria um novo dispositivo e associa a um leilão")
    @ApiResponse(responseCode = "201", description = "Dispositivo criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Leilão não encontrado ou dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Post("/{leilaoId}")
    public HttpResponse<List<DispositivoDTO>> criarDispositivos(@Body List<DispositivoDTO> dispositivosDTO, @PathVariable Long leilaoId) {
        List<DispositivoDTO> criados = dispositivoService.criarDispositivos(dispositivosDTO, leilaoId);
        return HttpResponse.status(HttpStatus.CREATED).body(criados);
    }

    @Operation(summary = "Busca um dispositivo por ID")
    @ApiResponse(responseCode = "200", description = "Dispositivo encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{id}")
    public HttpResponse<DispositivoDTO> buscarDispositivo(@PathVariable Long id) {
        return dispositivoService.buscarDispositivo(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um dispositivo existente")
    @ApiResponse(responseCode = "200", description = "Dispositivo atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}")
    public HttpResponse<DispositivoDTO> atualizarDispositivo(@PathVariable Long id, @Body DispositivoDTO dispositivoDTO) {
        return dispositivoService.atualizarDispositivo(id, dispositivoDTO)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Remove um dispositivo por ID")
    @ApiResponse(responseCode = "204", description = "Dispositivo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Delete("/{id}")
    public HttpResponse<Void> removerDispositivo(@PathVariable Long id) {
        if (dispositivoService.removerDispositivo(id)) {
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reassocia um dispositivo a um novo leilão")
    @ApiResponse(responseCode = "200", description = "Dispositivo reassociado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Erro de validação no leilão ou dispositivo")
    @ApiResponse(responseCode = "404", description = "Dispositivo ou leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}/reassociar/{novoLeilaoId}")
    public HttpResponse<DispositivoDTO> reassociarDispositivo(@PathVariable Long id, @PathVariable Long novoLeilaoId) {
        DispositivoDTO reassociado = dispositivoService.reassociarDispositivo(id, novoLeilaoId);
        return HttpResponse.ok().body(reassociado);
    }
}
