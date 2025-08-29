package br.ada.t1431.pix.domain.exception;

/**
 * Exceção lançada quando uma chave Pix não é encontrada no repositório.
 */
public class ChaveNaoEncontradaException extends RuntimeException{
    /**
     * Construtor padrão que define a mensagem de chave não encontrada.
     */
    public ChaveNaoEncontradaException() {
        super("Chave não encontrada.");
    }

}
