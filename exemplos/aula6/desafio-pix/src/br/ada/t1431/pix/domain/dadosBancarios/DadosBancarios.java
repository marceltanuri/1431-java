package br.ada.t1431.pix.domain.dadosBancarios;

import java.util.Objects;

public record DadosBancarios(
        String agencia,
        String conta,
        String codigoBanco,
        TipoDeContaBancaria tipoDeContaBancaria
) {
    public DadosBancarios {
        Objects.requireNonNull(agencia, "Agência não pode ser nula");
        Objects.requireNonNull(conta, "Conta não pode ser nula");
        Objects.requireNonNull(codigoBanco, "Código do banco não pode ser nulo");
        Objects.requireNonNull(tipoDeContaBancaria, "Tipo de conta não pode ser nulo");
    }
}
