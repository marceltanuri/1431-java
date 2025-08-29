package br.ada.t1431.pix.domain.validador.impl;

import br.ada.t1431.pix.domain.exception.ChavePixInvalidaException;
import br.ada.t1431.pix.domain.validador.Validador;

/**
 * Validador para chaves Pix do tipo Celular.
 */
public class ValidadorCelular implements Validador {
    /**
     * Valida se o valor informado é um número de celular válido.
     * 
     * @param valor valor da chave Pix
     * @throws ChavePixInvalidaException se o formato for inválido
     */
    @Override
    public void validar(String valor) throws ChavePixInvalidaException {
        final String REGEX_FORMATO_CELULAR = "^\\d{11}$";
        final String MENSAGEM_FORMATO_INVALIDO = "Formato de celular inválido. Esperado 11 dígitos numéricos (ex: 11987654321).";

        if (valor == null || !valor.matches(REGEX_FORMATO_CELULAR)) {
            throw new ChavePixInvalidaException(MENSAGEM_FORMATO_INVALIDO);
        }
    }
}
