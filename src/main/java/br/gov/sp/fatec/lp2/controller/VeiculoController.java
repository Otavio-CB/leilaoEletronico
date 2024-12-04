package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.dto.VeiculoDTO;
import br.gov.sp.fatec.lp2.service.VeiculoService;
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

@Controller("/veiculos")
@Tag(name = "Veículo", description = "Operações relacionadas aos veículos")
public class VeiculoController {

    @Inject
    private VeiculoService veiculoService;

    @Operation(summary = "Cria um novo veículo e o associa a um leilão")
    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Post("/{leilaoId}")
    public HttpResponse<List<VeiculoDTO>> criarVeiculos(@Body List<VeiculoDTO> veiculosDTO, @PathVariable Long leilaoId) {
        List<VeiculoDTO> criados = veiculoService.criarVeiculos(veiculosDTO, leilaoId);
        return HttpResponse.status(HttpStatus.CREATED).body(criados);
    }

    @Operation(summary = "Busca um veículo por ID")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{id}")
    public HttpResponse<VeiculoDTO> buscarVeiculo(@PathVariable Long id) {
        return veiculoService.buscarVeiculo(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um veículo existente")
    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}")
    public HttpResponse<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @Body VeiculoDTO veiculoDTO) {
        return veiculoService.atualizarVeiculo(id, veiculoDTO)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));

    }

    @Operation(summary = "Remove um veículo por ID")
    @ApiResponse(responseCode = "204", description = "Veículo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Delete("/{id}")
    public HttpResponse<Void> removerVeiculo(@PathVariable Long id) {
        if (veiculoService.removerVeiculo(id)) {
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reassocia um veículo a um novo leilão")
    @ApiResponse(responseCode = "200", description = "Veículo reassociado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Não é possível reassociar um veículo vendido ou o novo leilão já ocorreu")
    @ApiResponse(responseCode = "404", description = "Veículo ou leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}/reassociar/{novoLeilaoId}")
    public HttpResponse<VeiculoDTO> reassociarVeiculo(@PathVariable Long id, @PathVariable Long novoLeilaoId) {
        VeiculoDTO reassociado = veiculoService.reassociarVeiculo(id, novoLeilaoId);
        return HttpResponse.ok().body(reassociado);
    }
}
