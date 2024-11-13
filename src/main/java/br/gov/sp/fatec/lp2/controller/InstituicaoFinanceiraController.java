package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.dto.InstituicaoFinanceiraDTO;
import br.gov.sp.fatec.lp2.service.InstituicaoFinanceiraService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;


@Controller("/instituicoes")
@Tag(name = "Instituição Financeira", description = "Operações relacionadas às instituições financeiras")
public class InstituicaoFinanceiraController {

    @Inject
    private InstituicaoFinanceiraService instituicaoFinanceiraService;

    @Operation(summary = "Cria uma nova instituição financeira")
    @ApiResponse(responseCode = "201", description = "Instituição financeira criada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstituicaoFinanceiraDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Post
    public HttpResponse<InstituicaoFinanceiraDTO> criarInstituicaoFinanceira(@Body InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        InstituicaoFinanceiraDTO criado = instituicaoFinanceiraService.criarInstituicaoFinanceira(instituicaoFinanceiraDTO);
        return HttpResponse.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Busca uma instituição financeira por ID")
    @ApiResponse(responseCode = "200", description = "Instituição financeira encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstituicaoFinanceiraDTO.class)))
    @ApiResponse(responseCode = "404", description = "Instituição financeira não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{id}")
    public HttpResponse<InstituicaoFinanceiraDTO> buscarInstituicaoFinanceira(@PathVariable Long id) {
        return instituicaoFinanceiraService.buscarInstituicaoFinanceira(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza uma instituição financeira existente")
    @ApiResponse(responseCode = "200", description = "Instituição financeira atualizada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstituicaoFinanceiraDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Instituição financeira não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}")
    public HttpResponse<InstituicaoFinanceiraDTO> atualizarInstituicaoFinanceira(@PathVariable Long id, @Body InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        return instituicaoFinanceiraService.atualizarInstituicaoFinanceira(id, instituicaoFinanceiraDTO)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Remove uma instituição financeira por ID")
    @ApiResponse(responseCode = "204", description = "Instituição financeira removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Instituição financeira não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Delete("/{id}")
    public HttpResponse<Void> removerInstituicaoFinanceira(@PathVariable Long id) {
        if (instituicaoFinanceiraService.removerInstituicaoFinanceira(id)) {
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }
}
