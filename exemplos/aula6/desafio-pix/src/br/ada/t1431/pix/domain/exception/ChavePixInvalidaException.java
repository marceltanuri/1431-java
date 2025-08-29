package br.ada.t1431.pix.domain.exception;

/**
 * Exceção lançada quando uma chave Pix é considerada inválida.
 * Pode ser utilizada para indicar erros de validação de formato ou regras de negócio.
 */
public class ChavePixInvalidaException extends RuntimeException {

    /**
     * Construtor padrão.
     */
    public ChavePixInvalidaException() {
    }

    /**
     * Construtor com mensagem de erro.
     * @param message mensagem descritiva do erro
     */
    public ChavePixInvalidaException(String message) {
        super(message);
    }

    /**
     * Construtor com mensagem e causa.
     * @param message mensagem descritiva do erro
     * @param cause causa da exceção
     */
    public ChavePixInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construtor com causa.
     * @param cause causa da exceção
     */
    public ChavePixInvalidaException(Throwable cause) {
        super(cause);
    }

    /**
     * Construtor completo.
     * @param message mensagem descritiva do erro
     * @param cause causa da exceção
     * @param enableSuppression habilita supressão
     * @param writableStackTrace pilha de execução gravável
     */
    public ChavePixInvalidaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
