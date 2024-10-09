package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.dto.ClienteDTO;
import br.gov.sp.fatec.lp2.repository.ClienteRepository;
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

@Controller("/clientes")
@Tag(name = "Cliente", description = "Operações relacionadas aos clientes")
public class ClienteController {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    ModelMapper modelMapper;

    @Operation(summary = "Cria um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @Post
    public HttpResponse<ClienteDTO> criarCliente(@Body ClienteDTO clienteDTO) {
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        cliente = clienteRepository.save(cliente);
        ClienteDTO responseDTO = modelMapper.map(cliente, ClienteDTO.class);
        return HttpResponse.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Busca um cliente por ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteDTO.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @Get("/{id}")
    public HttpResponse<ClienteDTO> buscarCliente(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> HttpResponse.ok(modelMapper.map(cliente, ClienteDTO.class)))
                .orElse(HttpResponse.status(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualiza um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteDTO.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @Put("/{id}")
    public HttpResponse<ClienteDTO> atualizarCliente(@PathVariable Long id, @Body ClienteDTO clienteDTO) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
            cliente.setId(id);
            cliente = clienteRepository.update(cliente);
            return HttpResponse.ok(modelMapper.map(cliente, ClienteDTO.class));
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }

    public HttpResponse<Void> removerCliente(@PathVariable Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            clienteRepository.deleteById(id);
            return HttpResponse.noContent();
        }
        return HttpResponse.status(HttpStatus.NOT_FOUND);
    }
}
