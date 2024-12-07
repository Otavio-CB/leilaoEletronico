package br.gov.sp.fatec.lp2.exceptions.leilao;

public class LeilaoComAssociacoesException extends RuntimeException {
  public LeilaoComAssociacoesException(Long leilaoId) {
    super("O leilão com ID " + leilaoId + " não pode ser removido porque há produtos associados a ele.");
  }
}
