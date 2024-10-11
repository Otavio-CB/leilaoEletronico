package br.gov.sp.fatec.lp2.service;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDTO;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class LeilaoService {

    @Inject
    private LeilaoRepository leilaoRepository;

    @Inject
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Inject
    private ModelMapper modelMapper;

    public LeilaoDTO criarLeilao(LeilaoDTO leilaoDTO) {
        Leilao leilao = modelMapper.map(leilaoDTO, Leilao.class);

        List<InstituicaoFinanceira> instituicoes = leilaoDTO.getInstituicaoFinanceiraIds().stream()
                .map(id -> instituicaoFinanceiraRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Instituição financeira não encontrada")))
                .collect(Collectors.toList());

        leilao.setInstituicoesFinanceiras(instituicoes);
        Leilao leilaoSalvo = leilaoRepository.save(leilao);
        return modelMapper.map(leilaoSalvo, LeilaoDTO.class);
    }

    public Optional<LeilaoDTO> buscarLeilao(Long id) {
        return leilaoRepository.findById(id)
                .map(leilao -> modelMapper.map(leilao, LeilaoDTO.class));
    }

    public Optional<LeilaoDTO> atualizarLeilao(Long id, LeilaoDTO leilaoDTO) {
        return leilaoRepository.findById(id).map(leilaoExistente -> {
            Leilao leilao = modelMapper.map(leilaoDTO, Leilao.class);
            leilao.setId(id);
            Leilao atualizado = leilaoRepository.update(leilao);
            return modelMapper.map(atualizado, LeilaoDTO.class);
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
}
