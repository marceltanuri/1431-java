package br.ada.t1431.pix.domain.exception;

public class ChaveNaoEncontradaException extends RuntimeException{
    public ChaveNaoEncontradaException() {
        super("Chave não encontrada.");
    }

}
