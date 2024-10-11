package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.dto.ClienteDTO;
import br.gov.sp.fatec.lp2.repository.ClienteRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Singleton
public class ClienteService {

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ModelMapper modelMapper;

    public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        cliente = clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    public Optional<ClienteDTO> buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class));
    }

    public Optional<ClienteDTO> atualizarCliente(Long id, ClienteDTO clienteDTO) {
        return clienteRepository.findById(id).map(clienteExistente -> {
            Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
            cliente.setId(id);
            Cliente atualizado = clienteRepository.update(cliente);
            return modelMapper.map(atualizado, ClienteDTO.class);
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
