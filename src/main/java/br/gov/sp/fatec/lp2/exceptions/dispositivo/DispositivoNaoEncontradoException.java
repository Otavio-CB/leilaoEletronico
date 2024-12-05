package br.gov.sp.fatec.lp2.exceptions.dispositivo;

public class DispositivoNaoEncontradoException extends RuntimeException {
  public DispositivoNaoEncontradoException(Long id) {
    super("Dispositivo com ID " + id + " não encontrado");
  }
}
