package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.dto.ClienteDTO;
import br.gov.sp.fatec.lp2.service.ClienteService;
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


@Controller("/clientes")
@Tag(name = "Cliente", description = "Operações relacionadas aos clientes")
public class ClienteController {

    @Inject
    private ClienteService clienteService;

    @Operation(summary = "Cria um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Post
    public HttpResponse<List<ClienteDTO>> criarClientes(@Body List<ClienteDTO> clienteDTOs) {
        List<ClienteDTO> criados = clienteService.criarClientes(clienteDTOs);
        return HttpResponse.status(HttpStatus.CREATED).body(criados);
    }

    @Operation(summary = "Busca um cliente por ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteDTO.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Get("/{id}")
    public HttpResponse<ClienteDTO> buscarCliente(@PathVariable Long id) {
        return clienteService.buscarCliente(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Put("/{id}")
    public HttpResponse<ClienteDTO> atualizarCliente(@PathVariable Long id, @Body ClienteDTO clienteDTO) {
        return clienteService.atualizarCliente(id, clienteDTO)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Remove um cliente existente")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @Delete("/{id}")
    public HttpResponse<Void> removerCliente(@PathVariable Long id) {
        if (clienteService.removerCliente(id)) {
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }
}
