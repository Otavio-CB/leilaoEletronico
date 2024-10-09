package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.dto.ClienteDTO;
import br.gov.sp.fatec.lp2.repository.ClienteRepository;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Controller("/clientes")
public class ClienteController {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    ModelMapper modelMapper;

    @Post
    public ClienteDTO criarCliente(@Body ClienteDTO clienteDTO) {
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        cliente = clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    @Get("/{id}")
    public Optional<ClienteDTO> buscarCliente(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class));
    }

    @Put("/{id}")
    public ClienteDTO atualizarCliente(@PathVariable Long id, @Body ClienteDTO clienteDTO) {
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        cliente.setId(id);
        cliente = clienteRepository.update(cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    @Delete("/{id}")
    public void removerCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }
}
