package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.dto.ClienteDTO;
import br.gov.sp.fatec.lp2.mapper.ClienteMapper;
import br.gov.sp.fatec.lp2.repository.ClienteRepository;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Requires(beans = ClienteRepository.class)
@Singleton
public class ClienteService {

    @Inject
    private ClienteRepository clienteRepository;

    public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = ClienteMapper.INSTANCE.toEntity(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return ClienteMapper.INSTANCE.toDTO(cliente);
    }

    public Optional<ClienteDTO> buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .map(ClienteMapper.INSTANCE::toDTO);
    }

    public Optional<ClienteDTO> atualizarCliente(Long id, ClienteDTO clienteDTO) {
        return clienteRepository.findById(id).map(clienteExistente -> {
            Cliente cliente = ClienteMapper.INSTANCE.toEntity(clienteDTO);
            cliente.setId(id);
            Cliente atualizado = clienteRepository.update(cliente);
            return ClienteMapper.INSTANCE.toDTO(atualizado);
        });
    }

    public boolean removerCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
