package br.gov.sp.fatec.lp2.exceptions.cliente;

public class ErroAtualizacaoClienteException extends RuntimeException {
    public ErroAtualizacaoClienteException(String message) {
        super("Erro ao atualizar o cliente: " + message);
    }
}

