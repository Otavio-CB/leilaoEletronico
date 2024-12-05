package br.gov.sp.fatec.lp2.exceptions.leilao;

public class LeilaoOcorridoException extends RuntimeException {
    public LeilaoOcorridoException() {
        super("O novo leilão já ocorreu");
    }
}
