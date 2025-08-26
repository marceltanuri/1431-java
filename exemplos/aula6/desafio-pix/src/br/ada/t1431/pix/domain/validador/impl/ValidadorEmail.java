package br.ada.t1431.pix.domain.validador.impl;

import br.ada.t1431.pix.domain.exception.ChavePixInvalidaException;
import br.ada.t1431.pix.domain.validador.Validador;

import java.util.regex.Pattern;

public class ValidadorEmail implements Validador {

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String MENSAGEM_FORMATO_INVALIDO = "Formato de e-mail inválido.";

    @Override
    public void validar(String valor) throws ChavePixInvalidaException {
        if (valor == null || !EMAIL_PATTERN.matcher(valor).matches()) {
            throw new ChavePixInvalidaException(MENSAGEM_FORMATO_INVALIDO);
        }
    }
}
