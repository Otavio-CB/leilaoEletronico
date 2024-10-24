package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.Veiculo;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDTO;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDetalhadoDTO;
import br.gov.sp.fatec.lp2.mapper.LeilaoDetalhadoMapper;
import br.gov.sp.fatec.lp2.mapper.LeilaoMapper;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import io.micronaut.data.model.Sort;
import io.micronaut.data.model.Sort.Order;
import org.hibernate.Hibernate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
}