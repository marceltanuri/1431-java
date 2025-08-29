package br.ada.t1431.pix.domain.exception;

/**
 * Exceção lançada quando uma chave Pix duplicada é detectada no repositório.
 */
public class ChaveDuplicadaException extends RuntimeException {
    /**
     * Construtor que recebe uma mensagem descritiva do erro.
     * @param message mensagem de erro
     */
    public ChaveDuplicadaException(String message) {
        super(message);
    }
}
