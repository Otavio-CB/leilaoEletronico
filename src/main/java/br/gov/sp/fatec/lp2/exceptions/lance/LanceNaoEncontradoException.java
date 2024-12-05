package br.gov.sp.fatec.lp2.exceptions.lance;

public class LanceNaoEncontradoException extends RuntimeException {
    public LanceNaoEncontradoException(Long id) {
        super("Lance com ID " + id + " não encontrado");
    }
}
