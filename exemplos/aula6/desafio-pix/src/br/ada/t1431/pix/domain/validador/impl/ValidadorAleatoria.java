package br.ada.t1431.pix.domain.validador.impl;

import br.ada.t1431.pix.domain.exception.ChavePixInvalidaException;
import br.ada.t1431.pix.domain.validador.Validador;

/**
 * Validador para chaves Pix do tipo Aleatória.
 */
public class ValidadorAleatoria implements Validador {
    /**
     * Valida se o valor informado é uma chave aleatória válida.
     * @param valor valor da chave Pix
     * @throws ChavePixInvalidaException se o valor for inválido
     */
    @Override
    public void validar(String valor) throws ChavePixInvalidaException {

    }
}

// entrada dados (client) -> avaliar a intenção (controller) -> execução (service) -> saída (repository)

