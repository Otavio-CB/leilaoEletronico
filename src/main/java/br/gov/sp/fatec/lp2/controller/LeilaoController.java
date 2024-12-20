package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.*;
import br.gov.sp.fatec.lp2.mapper.LeilaoMapper;
import br.gov.sp.fatec.lp2.service.LeilaoService;
import br.gov.sp.fatec.lp2.utils.ExportUtil;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
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
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Post
    public HttpResponse<List<LeilaoDTO>> criarLeiloes(@Body List<LeilaoDTO> leiloesDTO) {
        List<LeilaoDTO> criados = leilaoService.criarLeiloes(leiloesDTO);
        return HttpResponse.status(HttpStatus.CREATED).body(criados);
    }

    @Operation(summary = "Busca um leilão por ID")
    @ApiResponse(responseCode = "200", description = "Leilão encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
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
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}")
    public HttpResponse<LeilaoDTO> atualizarLeilao(@PathVariable Long id, @Body LeilaoDTO leilaoDTO) {
        return leilaoService.atualizarLeilao(id, leilaoDTO)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Remove um leilão por ID")
    @ApiResponse(responseCode = "204", description = "Leilão removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
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
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão ou instituição financeira não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}/associar-instituicao/{instituicaoId}")
    public HttpResponse<Leilao> associarInstituicao(@PathVariable Long id, @PathVariable Long instituicaoId) {
        Leilao leilao = leilaoService.associarInstituicao(id, instituicaoId);
        return HttpResponse.ok(leilao);
    }

    @Operation(summary = "Lista todos os leilões ordenados por data de ocorrência")
    @ApiResponse(responseCode = "200", description = "Leilões listados com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/ordenados-por-data")
    public HttpResponse<List<LeilaoDTO>> listarLeiloesOrdenadosPorData() {
        List<LeilaoDTO> leiloes = leilaoService.listarLeiloesOrdenadosPorData().stream()
                .map(LeilaoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        return HttpResponse.ok(leiloes);
    }

    @Operation(summary = "Detalha um leilão")
    @ApiResponse(responseCode = "200", description = "Detalhes do leilão encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDetalhadoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{id}/detalhes")
    public HttpResponse<LeilaoDetalhadoDTO> detalharLeilao(@PathVariable Long id) {
        return leilaoService.detalharLeilao(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Detalha um dispositivo associado ao leilão")
    @ApiResponse(responseCode = "200", description = "Dispositivo encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DispositivoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Dispositivo ou leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{leilaoId}/dispositivos/{dispositivoId}")
    public HttpResponse<DispositivoDTO> detalharDispositivo(@PathVariable Long leilaoId, @PathVariable Long dispositivoId) {
        return leilaoService.detalharDispositivoNoLeilao(leilaoId, dispositivoId)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Detalha um veículo associado ao leilão")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Veículo ou leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{leilaoId}/veiculos/{veiculoId}")
    public HttpResponse<VeiculoDTO> detalharVeiculo(@PathVariable Long leilaoId, @PathVariable Long veiculoId) {
        return leilaoService.detalharVeiculoNoLeilao(leilaoId, veiculoId)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Busca produtos em um leilão por faixa de valor")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{leilaoId}/produtos/por-faixa-de-valor")
    public HttpResponse<List<Object>> buscarProdutosPorFaixaDeValor(
            @PathVariable Long leilaoId,
            @QueryValue Double valorMin,
            @QueryValue Double valorMax) {

        List<Object> produtos = leilaoService.buscarProdutosPorFaixaDeValor(leilaoId, valorMin, valorMax);
        return HttpResponse.ok(produtos);
    }

    @Operation(summary = "Busca produtos (veículos e dispositivos) em um leilão por faixa de valor considerando lances")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{leilaoId}/produtos/por-faixa-de-valor-com-lances")
    public HttpResponse<List<Object>> buscarProdutosPorFaixaDeValorComLances(
            @PathVariable Long leilaoId,
            @QueryValue Double valorMin,
            @QueryValue Double valorMax) {

        List<Object> produtos = leilaoService.buscarProdutosPorFaixaDeValorComLances(leilaoId, valorMin, valorMax);
        return HttpResponse.ok(produtos);
    }

    @Operation(summary = "Busca produtos em um leilão por palavra-chave")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{leilaoId}/produtos/por-palavra-chave")
    public List<Object> buscarProdutosPorPalavraChave(Long leilaoId, @QueryValue String palavraChave) {
        return leilaoService.buscarProdutosPorPalavraChave(leilaoId, palavraChave);
    }

    @Operation(summary = "Busca produtos em um leilão por tipo")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{leilaoId}/produtos/por-tipo")
    public HttpResponse<List<Object>> buscarProdutosPorTipo(
            @PathVariable Long leilaoId,
            @QueryValue String tipoProduto) {

        List<Object> produtos = leilaoService.buscarProdutosPorTipo(leilaoId, tipoProduto);
        return HttpResponse.ok(produtos);
    }

    @Operation(summary = "Obtém detalhes do leilão")
    @ApiResponse(responseCode = "200", description = "Detalhes do leilão encontrados",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoResumoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/detalhes-leilao/{leilaoId}")
    public LeilaoResumoDTO obterDetalhesLeilao(@PathVariable Long leilaoId) {
        return leilaoService.consultarDetalhesLeilao(leilaoId);
    }

    @Get("/exportar/{id}")
    public HttpResponse<String> exportarLeilao(@PathVariable Long id) {
        try {
            LeilaoDETDTO leilaoDETDTO = leilaoService.montarLeilaoDet(id);

            String projectPath = System.getProperty("user.dir");

            String filePath = projectPath + "/src/main/resources/det/leilao_" + id + ".DET";

            File directory = new File(projectPath + "/src/main/resources/det");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            ExportUtil.exportLeilaoExportacaoToDetFile(leilaoDETDTO, filePath);

            return HttpResponse.ok("Arquivo exportado com sucesso para: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return HttpResponse.serverError("Erro ao exportar arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            return HttpResponse.notFound("Leilão não encontrado: " + e.getMessage());
        }
    }
}
