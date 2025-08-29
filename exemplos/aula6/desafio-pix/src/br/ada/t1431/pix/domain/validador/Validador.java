package br.ada.t1431.pix.domain.validador;

import br.ada.t1431.pix.domain.exception.ChavePixInvalidaException;

/**
 * Interface para validadores de chaves Pix.
 * Implementações devem validar o formato e regras específicas de cada tipo de chave.
 */
public interface Validador {
    /**
     * Valida o valor da chave Pix.
     * @param valor valor da chave a ser validado
     * @throws ChavePixInvalidaException se o valor for inválido
     */
    void validar(String valor) throws ChavePixInvalidaException;
}