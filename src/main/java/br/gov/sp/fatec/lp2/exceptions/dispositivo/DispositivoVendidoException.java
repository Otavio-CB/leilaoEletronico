package br.gov.sp.fatec.lp2.exceptions.dispositivo;

public class DispositivoVendidoException extends RuntimeException {
    public DispositivoVendidoException() {
        super("Dispositivo já foi vendido");
    }
}
