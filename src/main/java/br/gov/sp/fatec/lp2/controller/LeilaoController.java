package br.gov.sp.fatec.lp2.controller;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import br.gov.sp.fatec.lp2.entity.Leilao;
import br.gov.sp.fatec.lp2.entity.dto.LeilaoDTO;
import br.gov.sp.fatec.lp2.repository.InstituicaoFinanceiraRepository;
import br.gov.sp.fatec.lp2.repository.LeilaoRepository;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/leiloes")
public class LeilaoController {

    @Inject
    LeilaoRepository leilaoRepository;

    @Inject
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Inject
    private ModelMapper modelMapper;

    @Post
    public LeilaoDTO criarLeilao(@Body LeilaoDTO leilaoDTO) {
        Leilao leilao = modelMapper.map(leilaoDTO, Leilao.class);

        List<InstituicaoFinanceira> instituicoes = leilaoDTO.getInstituicaoFinanceiraIds().stream()
                .map(id -> instituicaoFinanceiraRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Instituição financeira não encontrada")))
                .collect(Collectors.toList());

        leilao.setInstituicoesFinanceiras(instituicoes);

        return modelMapper.map(leilaoRepository.save(leilao), LeilaoDTO.class);
    }


    @Get("/{id}")
    public Optional<LeilaoDTO> buscarLeilao(@PathVariable Long id) {
        return leilaoRepository.findById(id)
                .map(leilao -> modelMapper.map(leilao, LeilaoDTO.class));
    }

    @Put("/{id}")
    public LeilaoDTO atualizarLeilao(@PathVariable Long id, @Body LeilaoDTO leilaoDTO) {
        Leilao leilao = modelMapper.map(leilaoDTO, Leilao.class);
        leilao.setId(id);
        return modelMapper.map(leilaoRepository.update(leilao), LeilaoDTO.class);
    }


    @Delete("/{id}")
    public void removerLeilao(@PathVariable Long id) {
        leilaoRepository.deleteById(id);
    }
}
