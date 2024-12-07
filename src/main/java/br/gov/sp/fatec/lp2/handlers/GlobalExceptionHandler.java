package br.gov.sp.fatec.lp2.handlers;

import br.gov.sp.fatec.lp2.exceptions.cliente.ClienteNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.cliente.ErroAtualizacaoClienteException;
import br.gov.sp.fatec.lp2.exceptions.cliente.ErroRemocaoClienteException;
import br.gov.sp.fatec.lp2.exceptions.dispositivo.DispositivoNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.dispositivo.DispositivoVendidoException;
import br.gov.sp.fatec.lp2.exceptions.instituicaofinanceira.InstituicaoFinanceiraNaoEncontradaException;
import br.gov.sp.fatec.lp2.exceptions.lance.LanceNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.lance.ProdutoNaoAssociadoException;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoNaoEncontradoException;
import br.gov.sp.fatec.lp2.exceptions.leilao.LeilaoOcorridoException;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import lombok.Getter;

@Produces
@Singleton
@Replaces(io.micronaut.http.server.exceptions.ExceptionHandler.class)
public class GlobalExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, RuntimeException exception) {
        if (exception instanceof ClienteNaoEncontradoException) {
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.getCode(), exception.getMessage()));
        } else if (exception instanceof ErroAtualizacaoClienteException) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.getCode(), exception.getMessage()));
        } else if (exception instanceof ErroRemocaoClienteException) {
            return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), exception.getMessage()));
        } else if (exception instanceof DispositivoNaoEncontradoException) {
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } else if (exception instanceof LeilaoNaoEncontradoException) {
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } else if (exception instanceof DispositivoVendidoException) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } else if (exception instanceof LeilaoOcorridoException) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } else if (exception instanceof InstituicaoFinanceiraNaoEncontradaException) {
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } else if (exception instanceof LanceNaoEncontradoException) {
            return HttpResponse.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } else if (exception instanceof ProdutoNaoAssociadoException) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
        return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Erro inesperado: " + exception.getMessage()));
    }

    @Getter
    @Serdeable
    static class ErrorResponse {
        private final int status;
        private final String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

    }
}
