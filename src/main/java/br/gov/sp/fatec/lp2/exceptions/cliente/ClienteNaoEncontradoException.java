package br.gov.sp.fatec.lp2.exceptions.cliente;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(Long id) {
        super("Cliente com ID " + id + " não encontrado");
    }
}