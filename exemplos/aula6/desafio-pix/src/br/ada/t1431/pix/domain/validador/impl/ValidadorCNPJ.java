package br.ada.t1431.pix.domain.validador.impl;
import br.ada.t1431.pix.domain.exception.ChavePixInvalidaException;
import br.ada.t1431.pix.domain.validador.Validador;

public class ValidadorCNPJ implements Validador {
    @Override
    public void validar(String valor) throws ChavePixInvalidaException {
        final String REGEX_FORMATO_CNPJ = "\\d{14}";
        final String REGEX_DIGITOS_IGUAIS = "(\\d)\\1{13}";
        final String MENSAGEM_FORMATO_INVALIDO = "Formato de CNPJ inválido. Deve conter 14 dígitos numéricos e não podem ser todos iguais.";
        final String MENSAGEM_DIGITOS_VERIFICADORES_INVALIDOS = "CNPJ inválido. Os dígitos verificadores não conferem.";

        if (valor == null || !valor.matches(REGEX_FORMATO_CNPJ) || valor.matches(REGEX_DIGITOS_IGUAIS)) {
            throw new ChavePixInvalidaException(MENSAGEM_FORMATO_INVALIDO);
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int digito1 = calcularDigitoVerificador(valor, pesos1);

        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int digito2 = calcularDigitoVerificador(valor, pesos2);

        if (digito1 != (valor.charAt(12) - '0') || digito2 != (valor.charAt(13) - '0')) {
            throw new ChavePixInvalidaException(MENSAGEM_DIGITOS_VERIFICADORES_INVALIDOS);
        }
    }

    private int calcularDigitoVerificador(String cnpj, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}
