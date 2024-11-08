package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.*;
import br.gov.sp.fatec.lp2.entity.dto.DispositivoDTO;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDTO;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDetalhadoDTO;
import br.gov.sp.fatec.lp2.entity.dto.VeiculoDTO;
import br.gov.sp.fatec.lp2.mapper.DispositivoMapper;
import br.gov.sp.fatec.lp2.mapper.LeilaoDetalhadoMapper;
import br.gov.sp.fatec.lp2.mapper.LeilaoMapper;
import br.gov.sp.fatec.lp2.mapper.VeiculoMapper;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import io.micronaut.data.model.Sort;
import io.micronaut.data.model.Sort.Order;
import org.hibernate.Hibernate;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class LeilaoService {

    @Inject
    private LeilaoRepository leilaoRepository;

    @Inject
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    public LeilaoDTO criarLeilao(LeilaoDTO leilaoDTO) {
        Leilao leilao = LeilaoMapper.INSTANCE.toEntity(leilaoDTO);

        List<InstituicaoFinanceira> instituicoes = leilaoDTO.getInstituicaoFinanceiraIds().stream()
                .map(id -> instituicaoFinanceiraRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Instituição financeira não encontrada")))
                .collect(Collectors.toList());

        leilao.setInstituicoesFinanceiras(instituicoes);
        Leilao leilaoSalvo = leilaoRepository.save(leilao);
        return LeilaoMapper.INSTANCE.toDTO(leilaoSalvo);
    }

    public Optional<LeilaoDTO> buscarLeilao(Long id) {
        return leilaoRepository.findById(id)
                .map(LeilaoMapper.INSTANCE::toDTO);
    }

    public Optional<LeilaoDTO> atualizarLeilao(Long id, LeilaoDTO leilaoDTO) {
        return leilaoRepository.findById(id).map(leilaoExistente -> {
            Leilao leilao = LeilaoMapper.INSTANCE.toEntity(leilaoDTO);
            leilao.setId(id);
            Leilao atualizado = leilaoRepository.update(leilao);
            return LeilaoMapper.INSTANCE.toDTO(atualizado);
        });
    }

    public boolean removerLeilao(Long id) {
        Optional<Leilao> leilaoOpt = leilaoRepository.findById(id);
        if (leilaoOpt.isPresent()) {
            leilaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Leilao associarInstituicao(Long leilaoId, Long instituicaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));
        InstituicaoFinanceira instituicao = instituicaoFinanceiraRepository.findById(instituicaoId)
                .orElseThrow(() -> new RuntimeException("Instituição financeira não encontrada"));

        leilao.getInstituicoesFinanceiras().add(instituicao);
        return leilaoRepository.update(leilao);
    }

    public List<Leilao> listarLeiloesOrdenadosPorData() {
        return leilaoRepository.findAllLeiloesOrdenadosPorData();
    }

    @Transactional(readOnly = true)
    public Optional<LeilaoDetalhadoDTO> detalharLeilao(Long id) {
        return leilaoRepository.findById(id).map(leilao -> {
            // Inicializar coleções
            if (leilao.getDispositivos() != null) {
                Hibernate.initialize(leilao.getDispositivos());
            }
            if (leilao.getVeiculos() != null) {
                Hibernate.initialize(leilao.getVeiculos());
            }

            // Ordenação e mapeamento para DTO
            leilao.getDispositivos().sort(Comparator.comparing(Dispositivo::getNome));
            leilao.getVeiculos().sort(Comparator.comparing(Veiculo::getDescricao));

            return LeilaoDetalhadoMapper.INSTANCE.toDTO(leilao);
        });
    }

    @Transactional(readOnly = true)
    public Optional<DispositivoDTO> detalharDispositivoNoLeilao(Long leilaoId, Long dispositivoId) {
        return leilaoRepository.findById(leilaoId).flatMap(leilao ->
                leilao.getDispositivos().stream()
                        .filter(dispositivo -> dispositivo.getId().equals(dispositivoId))
                        .findFirst()
                        .map(DispositivoMapper.INSTANCE::toDTO)
        );
    }

    @Transactional(readOnly = true)
    public Optional<VeiculoDTO> detalharVeiculoNoLeilao(Long leilaoId, Long veiculoId) {
        return leilaoRepository.findById(leilaoId).flatMap(leilao ->
                leilao.getVeiculos().stream()
                        .filter(veiculo -> veiculo.getId().equals(veiculoId))
                        .findFirst()
                        .map(VeiculoMapper.INSTANCE::toDTO)
        );
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorFaixaDeValor(Long leilaoId, Double valorMin, Double valorMax) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        // Filtra dispositivos pela faixa de valores
        List<DispositivoDTO> dispositivosFiltrados = leilao.getDispositivos().stream()
                .filter(dispositivo -> dispositivo.getValorInicial() >= valorMin && dispositivo.getValorInicial() <= valorMax)
                .map(DispositivoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());

        // Filtra veículos pela faixa de valores
        List<VeiculoDTO> veiculosFiltrados = leilao.getVeiculos().stream()
                .filter(veiculo -> veiculo.getValorInicial() >= valorMin && veiculo.getValorInicial() <= valorMax)
                .map(VeiculoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());

        // Junta dispositivos e veículos em uma lista única de produtos
        List<Object> produtosFiltrados = new ArrayList<>();
        produtosFiltrados.addAll(dispositivosFiltrados);
        produtosFiltrados.addAll(veiculosFiltrados);

        return produtosFiltrados;
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorFaixaDeValorComLances(Long leilaoId, Double valorMin, Double valorMax) {
        // Busca o leilão
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        // Filtra dispositivos pela faixa de valores considerando lances adicionais
        List<DispositivoDTO> dispositivosFiltrados = leilao.getDispositivos().stream()
                .filter(dispositivo -> {
                    Double valorTotal = dispositivo.getValorInicial() +
                            dispositivo.getLances().stream()
                                    .mapToDouble(Lance::getValor)
                                    .sum();
                    // Filtra apenas os dispositivos que estão dentro da faixa de valor, incluindo limites
                    return (valorTotal > valorMin && valorTotal < valorMax) ||
                            valorTotal.equals(valorMin) || valorTotal.equals(valorMax);
                })
                .map(DispositivoMapper.INSTANCE::toDTO) // Mapeia para DTO
                .toList();

        // Filtra veículos pela faixa de valores considerando lances adicionais
        List<VeiculoDTO> veiculosFiltrados = leilao.getVeiculos().stream()
                .filter(veiculo -> {
                    Double valorTotal = veiculo.getValorInicial() +
                            veiculo.getLances().stream()
                                    .mapToDouble(Lance::getValor)
                                    .sum();
                    // Filtra apenas os veículos que estão dentro da faixa de valor, incluindo limites
                    return (valorTotal > valorMin && valorTotal < valorMax) ||
                            valorTotal.equals(valorMin) || valorTotal.equals(valorMax);
                })
                .map(VeiculoMapper.INSTANCE::toDTO) // Mapeia para DTO
                .toList();

        // Junta dispositivos e veículos em uma lista única de produtos
        List<Object> produtosFiltrados = new ArrayList<>();
        produtosFiltrados.addAll(dispositivosFiltrados); // Adiciona dispositivos filtrados
        produtosFiltrados.addAll(veiculosFiltrados); // Adiciona veículos filtrados

        return produtosFiltrados;
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorPalavraChave(Long leilaoId, String palavraChave) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        // Filtra dispositivos e veículos em listas separadas
        List<DispositivoDTO> dispositivosFiltrados = filtrarDispositivosPorPalavraChave(leilao, palavraChave);
        List<VeiculoDTO> veiculosFiltrados = filtrarVeiculosPorPalavraChave(leilao, palavraChave);

        // Junta dispositivos e veículos em uma lista única de produtos
        List<Object> produtosFiltrados = new ArrayList<>();
        produtosFiltrados.addAll(dispositivosFiltrados);
        produtosFiltrados.addAll(veiculosFiltrados);

        return produtosFiltrados;
    }

    private List<DispositivoDTO> filtrarDispositivosPorPalavraChave(Leilao leilao, String palavraChave) {
        return leilao.getDispositivos().stream()
                .filter(dispositivo -> dispositivo.getNome() != null && dispositivo.getNome().toLowerCase().contains(palavraChave.toLowerCase()))
                .map(DispositivoMapper.INSTANCE::toDTO)
                .toList();
    }

    private List<VeiculoDTO> filtrarVeiculosPorPalavraChave(Leilao leilao, String palavraChave) {
        return leilao.getVeiculos().stream()
                .filter(veiculo -> veiculo.getModelo() != null && veiculo.getModelo().toLowerCase().contains(palavraChave.toLowerCase()))
                .map(VeiculoMapper.INSTANCE::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorTipo(Long leilaoId, String tipoProduto) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        List<Object> produtosFiltrados = new ArrayList<>();

        // Filtra dispositivos pelo tipo
        List<DispositivoDTO> dispositivosFiltrados = leilao.getDispositivos().stream()
                .filter(dispositivo -> dispositivo.getTipo().toString().equalsIgnoreCase(tipoProduto))
                .map(DispositivoMapper.INSTANCE::toDTO)
                .toList();
        produtosFiltrados.addAll(dispositivosFiltrados);

        // Filtra veículos pelo tipo
        List<VeiculoDTO> veiculosFiltrados = leilao.getVeiculos().stream()
                .filter(veiculo -> veiculo.getTipo().toString().equalsIgnoreCase(tipoProduto))
                .map(VeiculoMapper.INSTANCE::toDTO)
                .toList();
        produtosFiltrados.addAll(veiculosFiltrados);

        return produtosFiltrados;
    }

}