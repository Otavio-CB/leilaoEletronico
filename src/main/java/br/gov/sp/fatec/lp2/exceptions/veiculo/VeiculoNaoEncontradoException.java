package br.gov.sp.fatec.lp2.exceptions.veiculo;

public class VeiculoNaoEncontradoException extends RuntimeException {
    public VeiculoNaoEncontradoException(Long id) {
        super("Veículo com ID " + id + " não encontrado");
    }
}
