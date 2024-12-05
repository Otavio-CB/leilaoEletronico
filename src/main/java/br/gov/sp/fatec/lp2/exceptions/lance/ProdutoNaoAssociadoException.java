package br.gov.sp.fatec.lp2.exceptions.lance;

public class ProdutoNaoAssociadoException extends RuntimeException {
  public ProdutoNaoAssociadoException() {
    super("É necessário associar um Veículo ou Dispositivo ao lance");
  }
}
