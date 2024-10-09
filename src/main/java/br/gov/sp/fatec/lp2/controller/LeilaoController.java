package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDTO;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/leiloes")
@Tag(name = "Leilão", description = "Operações relacionadas aos leilões")
public class LeilaoController {

    @Inject
    LeilaoRepository leilaoRepository;

    @Inject
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Inject
    private ModelMapper modelMapper;

    @Operation(summary = "Cria um novo leilão")
    @ApiResponse(responseCode = "201", description = "Leilão criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @Post
    public HttpResponse<LeilaoDTO> criarLeilao(@Body LeilaoDTO leilaoDTO) {
        Leilao leilao = modelMapper.map(leilaoDTO, Leilao.class);

        List<InstituicaoFinanceira> instituicoes = leilaoDTO.getInstituicaoFinanceiraIds().stream()
                .map(id -> instituicaoFinanceiraRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Instituição financeira não encontrada")))
                .collect(Collectors.toList());

        leilao.setInstituicoesFinanceiras(instituicoes);

        Leilao leilaoSalvo = leilaoRepository.save(leilao);
        return HttpResponse.status(HttpStatus.CREATED).body(modelMapper.map(leilaoSalvo, LeilaoDTO.class));
    }

    @Operation(summary = "Busca um leilão por ID")
    @ApiResponse(responseCode = "200", description = "Leilão encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @Get("/{id}")
    public HttpResponse<LeilaoDTO> buscarLeilao(@PathVariable Long id) {
        return leilaoRepository.findById(id)
                .map(leilao -> HttpResponse.ok(modelMapper.map(leilao, LeilaoDTO.class)))
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um leilão existente")
    @ApiResponse(responseCode = "200", description = "Leilão atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeilaoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @Put("/{id}")
    public HttpResponse<LeilaoDTO> atualizarLeilao(@PathVariable Long id, @Body LeilaoDTO leilaoDTO) {
        Optional<Leilao> optionalLeilao = leilaoRepository.findById(id);
        if (optionalLeilao.isPresent()) {
            Leilao leilao = modelMapper.map(leilaoDTO, Leilao.class);
            leilao.setId(id);
            Leilao leilaoAtualizado = leilaoRepository.update(leilao);
            return HttpResponse.ok(modelMapper.map(leilaoAtualizado, LeilaoDTO.class));
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Remove um leilão por ID")
    @ApiResponse(responseCode = "204", description = "Leilão removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @Delete("/{id}")
    public HttpResponse<Void> removerLeilao(@PathVariable Long id) {
        Optional<Leilao> optionalLeilao = leilaoRepository.findById(id);
        if (optionalLeilao.isPresent()) {
            leilaoRepository.deleteById(id);
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
        Leilao leilao = leilaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        InstituicaoFinanceira instituicao = instituicaoFinanceiraRepository.findById(instituicaoId)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        leilao.getInstituicoesFinanceiras().add(instituicao);
        return HttpResponse.ok(leilaoRepository.update(leilao));
    }
}
