package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.dto.LanceDTO;
import br.gov.sp.fatec.lp2.service.LanceService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

@Controller("/lances")
@Tag(name = "Lance", description = "Operações relacionadas aos lances")
public class LanceController {

    @Inject
    private LanceService lanceService;

    @Operation(summary = "Cria um novo lance")
    @ApiResponse(responseCode = "201", description = "Lance criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LanceDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @Post
    public HttpResponse<LanceDTO> criarLance(@Body LanceDTO lanceDTO) {
        LanceDTO criado = lanceService.criarLance(lanceDTO);
        return HttpResponse.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Busca um lance por ID")
    @ApiResponse(responseCode = "200", description = "Lance encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LanceDTO.class)))
    @ApiResponse(responseCode = "404", description = "Lance não encontrado")
    @Get("/{id}")
    public HttpResponse<LanceDTO> buscarLance(@PathVariable Long id) {
        return lanceService.buscarLance(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }
}
