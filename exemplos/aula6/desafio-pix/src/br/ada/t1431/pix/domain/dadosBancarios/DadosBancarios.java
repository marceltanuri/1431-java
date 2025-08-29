package br.ada.t1431.pix.domain.dadosBancarios;

import java.util.Objects;

/**
 * Record que representa os dados bancários associados a uma chave Pix.
 * Inclui agência, conta, código do banco e tipo de conta bancária.
 */
public record DadosBancarios(
        String agencia,
        String conta,
        String codigoBanco,
        TipoDeContaBancaria tipoDeContaBancaria
) {
    /**
     * Valida que todos os campos obrigatórios não são nulos.
     */
    public DadosBancarios {
        Objects.requireNonNull(agencia, "Agência não pode ser nula");
        Objects.requireNonNull(conta, "Conta não pode ser nula");
        Objects.requireNonNull(codigoBanco, "Código do banco não pode ser nulo");
        Objects.requireNonNull(tipoDeContaBancaria, "Tipo de conta não pode ser nulo");
    }
}
