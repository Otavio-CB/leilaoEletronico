package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.Veiculo;
import br.gov.sp.fatec.lp2.entity.dto.VeiculoDTO;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import br.gov.sp.fatec.lp2.repository.VeiculoRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Controller("/veiculos")
@Tag(name = "Veículo", description = "Operações relacionadas aos veículos")
public class VeiculoController {

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    LeilaoRepository leilaoRepository;

    @Inject
    private ModelMapper modelMapper;

    @Operation(summary = "Cria um novo veículo e o associa a um leilão")
    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Leilão não encontrado")
    @Post
    public HttpResponse<VeiculoDTO> criarVeiculo(@Body VeiculoDTO veiculoDTO, @PathVariable Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        Veiculo veiculo = modelMapper.map(veiculoDTO, Veiculo.class);
        veiculo.setLeilao(leilao);
        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);
        return HttpResponse.status(HttpStatus.CREATED).body(modelMapper.map(veiculoSalvo, VeiculoDTO.class));
    }

    @Operation(summary = "Busca um veículo por ID")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @Get("/{id}")
    public HttpResponse<VeiculoDTO> buscarVeiculo(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(veiculo -> HttpResponse.ok(modelMapper.map(veiculo, VeiculoDTO.class)))
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um veículo existente")
    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @Put("/{id}")
    public HttpResponse<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @Body VeiculoDTO veiculoDTO) {
        veiculoRepository.findById(id).orElseThrow(() -> new RuntimeException("Veículo não encontrado")); // Apenas verifica a existência

        Veiculo veiculo = modelMapper.map(veiculoDTO, Veiculo.class);
        veiculo.setId(id);
        return HttpResponse.ok(modelMapper.map(veiculoRepository.update(veiculo), VeiculoDTO.class));
    }


    @Operation(summary = "Remove um veículo por ID")
    @ApiResponse(responseCode = "204", description = "Veículo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @Delete("/{id}")
    public HttpResponse<Void> removerVeiculo(@PathVariable Long id) {
        veiculoRepository.findById(id).orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        veiculoRepository.deleteById(id);
        return HttpResponse.noContent();
    }


    @Operation(summary = "Reassocia um veículo a um novo leilão")
    @ApiResponse(responseCode = "200", description = "Veículo reassociado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Veiculo.class)))
    @ApiResponse(responseCode = "400", description = "Não é possível reassociar um veículo vendido ou o novo leilão já ocorreu")
    @ApiResponse(responseCode = "404", description = "Veículo ou leilão não encontrado")
    @Put("/{id}/reassociar/{novoLeilaoId}")
    public HttpResponse<Veiculo> reassociarVeiculo(@PathVariable Long id, @PathVariable Long novoLeilaoId) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (veiculo.isVendido()) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Leilao novoLeilao = leilaoRepository.findById(novoLeilaoId)
                .orElseThrow(() -> new RuntimeException("Novo leilão não encontrado"));

        if (novoLeilao.getDataOcorrencia().isBefore(LocalDateTime.now())) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(null);
        }

        veiculo.setLeilao(novoLeilao);
        return HttpResponse.ok(veiculoRepository.update(veiculo));
    }

}
