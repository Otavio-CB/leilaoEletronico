package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.*;
import br.gov.sp.fatec.lp2.entity.dto.*;
import br.gov.sp.fatec.lp2.entity.enums.StatusLeilao;
import br.gov.sp.fatec.lp2.exceptions.instituicaofinanceira.InstituicaoFinanceiraNaoEncontradaException;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoComAssociacoesException;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoNaoEncontradoException;
import br.gov.sp.fatec.lp2.mapper.*;
import br.gov.sp.fatec.lp2.repository.*;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.hibernate.Hibernate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class LeilaoService {

    @Inject
    private LanceRepository lanceRepository;

    @Inject
    private LeilaoRepository leilaoRepository;

    @Inject
    private VeiculoRepository veiculoRepository;

    @Inject
    private DispositivoRepository dispositivoRepository;

    @Inject
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Transactional
    public LeilaoDETDTO montarLeilaoDet(Long id) {
        Leilao leilao = leilaoRepository.findById(id).orElseThrow(() -> new LeilaoNaoEncontradoException(id));
        return LeilaoDETDTOMapper.INSTANCE.toLeilaoDETDTO(leilao);
    }

    public List<LeilaoDTO> criarLeiloes(List<LeilaoDTO> leiloesDTO) {
        List<Leilao> leiloes = leiloesDTO.stream().map(leilaoDTO -> {
            Leilao leilao = LeilaoMapper.INSTANCE.toEntity(leilaoDTO);

            List<InstituicaoFinanceira> instituicoes = leilaoDTO.getInstituicaoFinanceiraIds().stream().map(id -> instituicaoFinanceiraRepository.findById(id).orElseThrow(() -> new InstituicaoFinanceiraNaoEncontradaException(id))).collect(Collectors.toList());

            leilao.setInstituicoesFinanceiras(instituicoes);
            return leilao;
        }).collect(Collectors.toList());

        leiloes = leilaoRepository.saveAll(leiloes);
        return leiloes.stream().map(leilao -> LeilaoMapper.INSTANCE.toDTO(leilao)).collect(Collectors.toList());
    }

    public Optional<LeilaoDTO> buscarLeilao(Long id) {
        return leilaoRepository.findById(id).map(LeilaoMapper.INSTANCE::toDTO);
    }

    public Optional<LeilaoDTO> atualizarLeilao(Long id, LeilaoDTO leilaoDTO) {
        return leilaoRepository.findById(id).map(leilaoExistente -> {
            Leilao leilao = LeilaoMapper.INSTANCE.toEntity(leilaoDTO);
            leilao.setId(id);
            Leilao atualizado = leilaoRepository.update(leilao);
            return LeilaoMapper.INSTANCE.toDTO(atualizado);
        });
    }

    @Transactional
    public boolean removerLeilao(Long id) {
        if (!leilaoRepository.existsById(id)) {
            throw new LeilaoNaoEncontradoException(id);
        }

        Leilao leilao = leilaoRepository.findById(id).orElseThrow(() -> new LeilaoNaoEncontradoException(id));

        if (!leilao.getDispositivos().isEmpty() || !leilao.getVeiculos().isEmpty()) {
            throw new LeilaoComAssociacoesException(id);
        }

        leilaoRepository.deleteById(id);
        return true;
    }

    public Leilao associarInstituicao(Long leilaoId, Long instituicaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new LeilaoNaoEncontradoException(leilaoId));
        InstituicaoFinanceira instituicao = instituicaoFinanceiraRepository.findById(instituicaoId).orElseThrow(() -> new InstituicaoFinanceiraNaoEncontradaException(instituicaoId));

        leilao.getInstituicoesFinanceiras().add(instituicao);
        return leilaoRepository.update(leilao);
    }

    public List<Leilao> listarLeiloesOrdenadosPorData() {
        return leilaoRepository.findAllLeiloesOrdenadosPorData();
    }

    @Transactional(readOnly = true)
    public Optional<LeilaoDetalhadoDTO> detalharLeilao(Long id) {
        return leilaoRepository.findById(id).map(leilao -> {
            if (leilao.getDispositivos() != null) {
                Hibernate.initialize(leilao.getDispositivos());
            }
            if (leilao.getVeiculos() != null) {
                Hibernate.initialize(leilao.getVeiculos());
            }

            leilao.getDispositivos().sort(Comparator.comparing(Dispositivo::getNome));
            leilao.getVeiculos().sort(Comparator.comparing(Veiculo::getDescricao));

            return LeilaoDetalhadoMapper.INSTANCE.toDTO(leilao);
        });
    }

    @Transactional(readOnly = true)
    public Optional<DispositivoDTO> detalharDispositivoNoLeilao(Long leilaoId, Long dispositivoId) {
        return leilaoRepository.findById(leilaoId).flatMap(leilao -> leilao.getDispositivos().stream().filter(dispositivo -> dispositivo.getId().equals(dispositivoId)).findFirst().map(DispositivoMapper.INSTANCE::toDTO));
    }

    @Transactional(readOnly = true)
    public Optional<VeiculoDTO> detalharVeiculoNoLeilao(Long leilaoId, Long veiculoId) {
        return leilaoRepository.findById(leilaoId).flatMap(leilao -> leilao.getVeiculos().stream().filter(veiculo -> veiculo.getId().equals(veiculoId)).findFirst().map(VeiculoMapper.INSTANCE::toDTO));
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorFaixaDeValor(Long leilaoId, Double valorMin, Double valorMax) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        List<DispositivoDTO> dispositivosFiltrados = leilao.getDispositivos().stream().filter(dispositivo -> dispositivo.getValorInicial() >= valorMin && dispositivo.getValorInicial() <= valorMax).map(DispositivoMapper.INSTANCE::toDTO).collect(Collectors.toList());

        List<VeiculoDTO> veiculosFiltrados = leilao.getVeiculos().stream().filter(veiculo -> veiculo.getValorInicial() >= valorMin && veiculo.getValorInicial() <= valorMax).map(VeiculoMapper.INSTANCE::toDTO).collect(Collectors.toList());

        List<Object> produtosFiltrados = new ArrayList<>();
        produtosFiltrados.addAll(dispositivosFiltrados);
        produtosFiltrados.addAll(veiculosFiltrados);

        return produtosFiltrados;
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorFaixaDeValorComLances(Long leilaoId, Double valorMin, Double valorMax) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        List<DispositivoDTO> dispositivosFiltrados = leilao.getDispositivos().stream().filter(dispositivo -> {
            Double valorTotal = dispositivo.getValorInicial() + dispositivo.getLances().stream().mapToDouble(Lance::getValor).sum();
            return (valorTotal > valorMin && valorTotal < valorMax) || valorTotal.equals(valorMin) || valorTotal.equals(valorMax);
        }).map(DispositivoMapper.INSTANCE::toDTO).toList();

        List<VeiculoDTO> veiculosFiltrados = leilao.getVeiculos().stream().filter(veiculo -> {
            Double valorTotal = veiculo.getValorInicial() + veiculo.getLances().stream().mapToDouble(Lance::getValor).sum();
            return (valorTotal > valorMin && valorTotal < valorMax) || valorTotal.equals(valorMin) || valorTotal.equals(valorMax);
        }).map(VeiculoMapper.INSTANCE::toDTO).toList();

        List<Object> produtosFiltrados = new ArrayList<>();
        produtosFiltrados.addAll(dispositivosFiltrados);
        produtosFiltrados.addAll(veiculosFiltrados);

        return produtosFiltrados;
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorPalavraChave(Long leilaoId, String palavraChave) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        List<DispositivoDTO> dispositivosFiltrados = filtrarDispositivosPorPalavraChave(leilao, palavraChave);
        List<VeiculoDTO> veiculosFiltrados = filtrarVeiculosPorPalavraChave(leilao, palavraChave);

        List<Object> produtosFiltrados = new ArrayList<>();
        produtosFiltrados.addAll(dispositivosFiltrados);
        produtosFiltrados.addAll(veiculosFiltrados);

        return produtosFiltrados;
    }

    private List<DispositivoDTO> filtrarDispositivosPorPalavraChave(Leilao leilao, String palavraChave) {
        return leilao.getDispositivos().stream().filter(dispositivo -> dispositivo.getNome() != null && dispositivo.getNome().toLowerCase().contains(palavraChave.toLowerCase())).map(DispositivoMapper.INSTANCE::toDTO).toList();
    }

    private List<VeiculoDTO> filtrarVeiculosPorPalavraChave(Leilao leilao, String palavraChave) {
        return leilao.getVeiculos().stream().filter(veiculo -> veiculo.getModelo() != null && veiculo.getModelo().toLowerCase().contains(palavraChave.toLowerCase())).map(VeiculoMapper.INSTANCE::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<Object> buscarProdutosPorTipo(Long leilaoId, String tipoProduto) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

        List<Object> produtosFiltrados = new ArrayList<>();

        List<DispositivoDTO> dispositivosFiltrados = leilao.getDispositivos().stream().filter(dispositivo -> dispositivo.getTipo().toString().equalsIgnoreCase(tipoProduto)).map(DispositivoMapper.INSTANCE::toDTO).toList();
        produtosFiltrados.addAll(dispositivosFiltrados);

        List<VeiculoDTO> veiculosFiltrados = leilao.getVeiculos().stream().filter(veiculo -> veiculo.getTipo().toString().equalsIgnoreCase(tipoProduto)).map(VeiculoMapper.INSTANCE::toDTO).toList();
        produtosFiltrados.addAll(veiculosFiltrados);

        return produtosFiltrados;
    }

    @Transactional
    public LeilaoResumoDTO consultarDetalhesLeilao(Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId).orElseThrow(() -> new IllegalArgumentException("Leilão não encontrado."));

        ZoneId brasiliaZoneId = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime nowBrasilia = ZonedDateTime.now(brasiliaZoneId);
        ZonedDateTime dataOcorrenciaBrasilia = leilao.getDataOcorrencia().atZone(brasiliaZoneId);
        ZonedDateTime dataVisitaBrasilia = leilao.getDataVisita().atZone(brasiliaZoneId);

        if (nowBrasilia.isBefore(dataOcorrenciaBrasilia)) {
            leilao.setStatus(StatusLeilao.EM_ABERTO);
        } else if (nowBrasilia.isAfter(dataOcorrenciaBrasilia) && nowBrasilia.isBefore(dataVisitaBrasilia)) {
            leilao.setStatus(StatusLeilao.EM_ANDAMENTO);
        } else {
            leilao.setStatus(StatusLeilao.FINALIZADO);
        }

        List<ProdutoVencedorDTO> produtosVencedores = new ArrayList<>();

        List<Dispositivo> dispositivos = dispositivoRepository.findByLeilaoIdWithLances(leilaoId);
        for (Dispositivo dispositivo : dispositivos) {
            Lance lanceVencedor = buscarMaiorLance(dispositivo.getLances());
            if (lanceVencedor != null) {
                ProdutoVencedorDTO vencedorDTO = new ProdutoVencedorDTO();
                vencedorDTO.setProdutoId(dispositivo.getId());
                vencedorDTO.setProdutoDescricao(dispositivo.getDescricao());
                vencedorDTO.setValorVencedor(lanceVencedor.getValor());
                vencedorDTO.setClienteNome(lanceVencedor.getCliente().getNome());
                vencedorDTO.setTipoProduto("DISPOSITIVO");
                produtosVencedores.add(vencedorDTO);
            }
        }

        List<Veiculo> veiculos = veiculoRepository.findByLeilaoIdWithLances(leilaoId);
        for (Veiculo veiculo : veiculos) {
            Lance lanceVencedor = buscarMaiorLance(veiculo.getLances());
            if (lanceVencedor != null) {
                ProdutoVencedorDTO vencedorDTO = new ProdutoVencedorDTO();
                vencedorDTO.setProdutoId(veiculo.getId());
                vencedorDTO.setProdutoDescricao(veiculo.getDescricao());
                vencedorDTO.setValorVencedor(lanceVencedor.getValor());
                vencedorDTO.setClienteNome(lanceVencedor.getCliente().getNome());
                vencedorDTO.setTipoProduto("VEICULO");
                produtosVencedores.add(vencedorDTO);
            }
        }

        LeilaoResumoDTO resumoDTO = new LeilaoResumoDTO();
        resumoDTO.setLeilaoId(leilao.getId());
        resumoDTO.setStatus(leilao.getStatus());
        resumoDTO.setProdutosVencedores(produtosVencedores);

        return resumoDTO;
    }

    private Lance buscarMaiorLance(List<Lance> lances) {
        return lances.stream().max(Comparator.comparing(Lance::getValor)).orElse(null);
    }
}
