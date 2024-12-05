package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Cliente;
import br.gov.sp.fatec.lp2.entity.dto.ClienteDTO;
import br.gov.sp.fatec.lp2.exceptions.cliente.ClienteNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.cliente.ErroAtualizacaoClienteException;
import br.gov.sp.fatec.lp2.exceptions.cliente.ErroRemocaoClienteException;
import br.gov.sp.fatec.lp2.mapper.ClienteMapper;
import br.gov.sp.fatec.lp2.repository.ClienteRepository;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ClienteService {

    @Inject
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> criarClientes(List<ClienteDTO> clienteDTOs) {
        List<Cliente> clientes = clienteDTOs.stream()
                .map(clienteDTO -> ClienteMapper.INSTANCE.toEntity(clienteDTO))
                .collect(Collectors.toList());
        clientes = clienteRepository.saveAll(clientes);
        return clientes.stream()
                .map(cliente -> ClienteMapper.INSTANCE.toDTO(cliente))
                .collect(Collectors.toList());
    }

    public Optional<ClienteDTO> buscarCliente(Long id) {
        return Optional.ofNullable(clienteRepository.findById(id)
                .map(ClienteMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id)));
    }

    public Optional<ClienteDTO> atualizarCliente(Long id, ClienteDTO clienteDTO) {
        return Optional.ofNullable(clienteRepository.findById(id).map(clienteExistente -> {
            Cliente cliente = ClienteMapper.INSTANCE.toEntity(clienteDTO);
            cliente.setId(id);
            try {
                Cliente atualizado = clienteRepository.update(cliente);
                return ClienteMapper.INSTANCE.toDTO(atualizado);
            } catch (Exception e) {
                throw new ErroAtualizacaoClienteException(e.getMessage());
            }
        }).orElseThrow(() -> new ClienteNaoEncontradoException(id)));
    }

    public boolean removerCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            try {
                clienteRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new ErroRemocaoClienteException(id);
            }
        }
        throw new ClienteNaoEncontradoException(id);
    }
}
