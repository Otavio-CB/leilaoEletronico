package br.gov.sp.fatec.lp2.exceptions.leilao;

public class LeilaoNaoEncontradoException extends RuntimeException {
    public LeilaoNaoEncontradoException(Long id) {
        super("Leilão com ID " + id + " não encontrado");
    }
}
