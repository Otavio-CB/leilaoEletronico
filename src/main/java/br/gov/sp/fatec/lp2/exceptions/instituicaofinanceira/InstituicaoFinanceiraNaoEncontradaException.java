package br.gov.sp.fatec.lp2.exceptions.instituicaofinanceira;

public class InstituicaoFinanceiraNaoEncontradaException extends RuntimeException {
    public InstituicaoFinanceiraNaoEncontradaException(Long id) {
        super("Instituição Financeira com ID " + id + " não encontrada");
    }
}
