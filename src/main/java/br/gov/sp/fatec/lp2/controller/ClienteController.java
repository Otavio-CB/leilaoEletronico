package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.repository.ClienteRepository;
import br.gov.sp.fatec.lp2.entity.Cliente;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/clientes")
public class ClienteController {

    @Inject
    ClienteRepository clienteRepository;

    @Post
    public Cliente criarCliente(@Body Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Get("/{id}")
    public Optional<Cliente> buscarCliente(@PathVariable Long id) {
        return clienteRepository.findById(id);
    }

    @Put("/{id}")
    public Cliente atualizarCliente(@PathVariable Long id, @Body Cliente cliente) {
        cliente.setId(id);
        return clienteRepository.update(cliente);
    }

    @Delete("/{id}")
    public void removerCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }
}
