package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.dto.InstituicaoFinanceiraDTO;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
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

import java.util.Optional;

@Controller("/instituicoes")
@Tag(name = "Instituição Financeira", description = "Operações relacionadas às instituições financeiras")

public class InstituicaoFinanceiraController {

    @Inject
    InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Inject
    private ModelMapper modelMapper;

    @Operation(summary = "Cria uma nova instituição financeira")
    @ApiResponse(responseCode = "201", description = "Instituição financeira criada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstituicaoFinanceiraDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @Post
    public HttpResponse<InstituicaoFinanceiraDTO> criarInstituicaoFinanceira(@Body InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        InstituicaoFinanceira instituicaoFinanceira = modelMapper.map(instituicaoFinanceiraDTO, InstituicaoFinanceira.class);
        instituicaoFinanceira = instituicaoFinanceiraRepository.save(instituicaoFinanceira);
        InstituicaoFinanceiraDTO responseDTO = modelMapper.map(instituicaoFinanceira, InstituicaoFinanceiraDTO.class);
        return HttpResponse.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Busca uma instituição financeira por ID")
    @ApiResponse(responseCode = "200", description = "Instituição financeira encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstituicaoFinanceiraDTO.class)))
    @ApiResponse(responseCode = "404", description = "Instituição financeira não encontrada")
    @Get("/{id}")
    public HttpResponse<InstituicaoFinanceiraDTO> buscarInstituicaoFinanceira(@PathVariable Long id) {
        return instituicaoFinanceiraRepository.findById(id)
                .map(instituicao -> HttpResponse.ok(modelMapper.map(instituicao, InstituicaoFinanceiraDTO.class)))
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza uma instituição financeira existente")
    @ApiResponse(responseCode = "200", description = "Instituição financeira atualizada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstituicaoFinanceiraDTO.class)))
    @ApiResponse(responseCode = "404", description = "Instituição financeira não encontrada")
    @Put("/{id}")
    public HttpResponse<InstituicaoFinanceiraDTO> atualizarInstituicaoFinanceira(@PathVariable Long id, @Body InstituicaoFinanceiraDTO instituicaoFinanceiraDTO) {
        Optional<InstituicaoFinanceira> optionalInstituicao = instituicaoFinanceiraRepository.findById(id);
        if (optionalInstituicao.isPresent()) {
            InstituicaoFinanceira instituicao = modelMapper.map(instituicaoFinanceiraDTO, InstituicaoFinanceira.class);
            instituicao.setId(id);
            instituicao = instituicaoFinanceiraRepository.update(instituicao);
            return HttpResponse.ok(modelMapper.map(instituicao, InstituicaoFinanceiraDTO.class));
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Remove uma instituição financeira por ID")
    @ApiResponse(responseCode = "204", description = "Instituição financeira removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Instituição financeira não encontrada")
    @Delete("/{id}")
    public HttpResponse<Void> removerInstituicaoFinanceira(@PathVariable Long id) {
        Optional<InstituicaoFinanceira> optionalInstituicao = instituicaoFinanceiraRepository.findById(id);
        if (optionalInstituicao.isPresent()) {
            instituicaoFinanceiraRepository.deleteById(id);
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }
}
