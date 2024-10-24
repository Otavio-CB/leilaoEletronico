package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDTO;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDetalhadoDTO;
import br.gov.sp.fatec.lp2.mapper.LeilaoMapper;
import br.gov.sp.fatec.lp2.service.LeilaoService;
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
import java.util.stream.Collectors;

@Controller("/leiloes")
@Tag(name = "Leilão", description = "Operações relacionadas aos leilões")
public class LeilaoController {

    @Inject
    private LeilaoService leilaoService;

    @Operation(summary = "Cria um novo leilão")
    @ApiResponse(responseCode = "201", description = "Leilão criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @Post
    public HttpResponse<LeilaoDTO> criarLeilao(@Body LeilaoDTO leilaoDTO) {
        LeilaoDTO criado = leilaoService.criarLeilao(leilaoDTO);
        return HttpResponse.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Busca um leilão por ID")
    @ApiResponse(responseCode = "200", description = "Leilão encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @Get("/{id}")
    public HttpResponse<LeilaoDTO> buscarLeilao(@PathVariable Long id) {
        return leilaoService.buscarLeilao(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um leilão existente")
    @ApiResponse(responseCode = "200", description = "Leilão atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @Put("/{id}")
    public HttpResponse<LeilaoDTO> atualizarLeilao(@PathVariable Long id, @Body LeilaoDTO leilaoDTO) {
        return leilaoService.atualizarLeilao(id, leilaoDTO)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Remove um leilão por ID")
    @ApiResponse(responseCode = "204", description = "Leilão removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @Delete("/{id}")
    public HttpResponse<Void> removerLeilao(@PathVariable Long id) {
        if (leilaoService.removerLeilao(id)) {
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Associa uma instituição financeira a um leilão")
    @ApiResponse(responseCode = "200", description = "Instituição associada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Leilao.class)))
    @ApiResponse(responseCode = "404", description = "Leilão ou instituição financeira não encontrada")
    @Put("/{id}/associar-instituicao/{instituicaoId}")
    public HttpResponse<Leilao> associarInstituicao(@PathVariable Long id, @PathVariable Long instituicaoId) {
        Leilao leilao = leilaoService.associarInstituicao(id, instituicaoId);
        return HttpResponse.ok(leilao);
    }

    @Operation(summary = "Lista todos os leilões ordenados por data de ocorrência")
    @ApiResponse(responseCode = "200", description = "Leilões listados com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Leilao.class)))
    @Get("/ordenados-por-data")
    public HttpResponse<List<LeilaoDTO>> listarLeiloesOrdenadosPorData() {
        List<LeilaoDTO> leiloes = leilaoService.listarLeiloesOrdenadosPorData().stream()
                .map(LeilaoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        return HttpResponse.ok(leiloes);
    }

    @Get("/{id}/detalhes")
    public HttpResponse<LeilaoDetalhadoDTO> detalharLeilao(@PathVariable Long id) {
        return leilaoService.detalharLeilao(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

}