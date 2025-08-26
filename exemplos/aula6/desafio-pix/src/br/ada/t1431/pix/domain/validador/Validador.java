package br.ada.t1431.pix.domain.validador;

import br.ada.t1431.pix.domain.exception.ChavePixInvalidaException;

public interface Validador {

    void validar(String valor) throws ChavePixInvalidaException;

}