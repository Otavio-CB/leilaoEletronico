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

    @Operation(summary = "Registra um novo lance")
    @ApiResponse(responseCode = "201", description = "Lance criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LanceDTO.class)))
    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados fornecidos")
    @Post
    public HttpResponse<LanceDTO> criarLance(@Body LanceDTO lanceDTO) {
        LanceDTO criado = lanceService.salvarLance(lanceDTO);
        return HttpResponse.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Busca todos os lances registrados")
    @ApiResponse(responseCode = "200", description = "Lances retornados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LanceDTO.class)))
    @Get
    public HttpResponse<Iterable<LanceDTO>> buscarLances() {
        return HttpResponse.ok(lanceService.buscarTodos());
    }

    @Operation(summary = "Atualiza um lance existente")
    @ApiResponse(responseCode = "200", description = "Lance atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LanceDTO.class)))
    @ApiResponse(responseCode = "404", description = "Lance não encontrado")
    @Put("/{id}")
    public HttpResponse<LanceDTO> atualizarLance(@PathVariable Long id, @Body LanceDTO lanceDTO) {
        return HttpResponse.ok(lanceService.atualizarLance(id, lanceDTO));
    }

    @Operation(summary = "Remove um lance por ID")
    @ApiResponse(responseCode = "204", description = "Lance removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Lance não encontrado")
    @Delete("/{id}")
    public HttpResponse<Void> removerLance(@PathVariable Long id) {
        lanceService.removerLance(id);
        return HttpResponse.noContent();
    }
}
