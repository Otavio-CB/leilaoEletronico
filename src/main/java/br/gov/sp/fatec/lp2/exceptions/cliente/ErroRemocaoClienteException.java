package br.gov.sp.fatec.lp2.exceptions.cliente;

public class ErroRemocaoClienteException extends RuntimeException {
    public ErroRemocaoClienteException(Long id) {
        super("Erro ao remover o cliente com ID " + id);
    }
}
