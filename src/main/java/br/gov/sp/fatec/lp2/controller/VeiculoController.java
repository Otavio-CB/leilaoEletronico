package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Veiculo;
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
    @Post("/{leilaoId}")
    public HttpResponse<VeiculoDTO> criarVeiculo(@Body VeiculoDTO veiculoDTO, @PathVariable Long leilaoId) {
        VeiculoDTO criado = veiculoService.criarVeiculo(veiculoDTO, leilaoId);
        return HttpResponse.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Busca um veículo por ID")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
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
    @Put("/{id}")
    public HttpResponse<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @Body VeiculoDTO veiculoDTO) {
        VeiculoDTO atualizado = veiculoService.atualizarVeiculo(id, veiculoDTO);
        return HttpResponse.ok(atualizado);
    }

    @Operation(summary = "Remove um veículo por ID")
    @ApiResponse(responseCode = "204", description = "Veículo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @Delete("/{id}")
    public HttpResponse<Void> removerVeiculo(@PathVariable Long id) {
        if (veiculoService.removerVeiculo(id)) {
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reassocia um veículo a um novo leilão")
    @ApiResponse(responseCode = "200", description = "Veículo reassociado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Veiculo.class)))
    @ApiResponse(responseCode = "400", description = "Não é possível reassociar um veículo vendido ou o novo leilão já ocorreu")
    @ApiResponse(responseCode = "404", description = "Veículo ou leilão não encontrado")
    @Put("/{id}/reassociar/{novoLeilaoId}")
    public HttpResponse<Veiculo> reassociarVeiculo(@PathVariable Long id, @PathVariable Long novoLeilaoId) {
        try {
            Veiculo veiculo = veiculoService.reassociarVeiculo(id, novoLeilaoId);
            return HttpResponse.ok(veiculo);
        } catch (RuntimeException e) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
