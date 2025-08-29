package br.ada.t1431.pix.infrastructure.repository;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.Status;
import br.ada.t1431.pix.domain.TipoDeChavePix;
import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.dadosBancarios.TipoDeContaBancaria;

import java.time.LocalDateTime;

public class ChavePixMapper {

    public static ChavePix from(String[] columns) {

        String codigoInstituicao = columns[0];
        String agencia = columns[1];
        String conta = columns[2];
        String tipoConta = columns[3];

        DadosBancarios dadosBancarios = new DadosBancarios(agencia, conta, codigoInstituicao, TipoDeContaBancaria.valueOf(tipoConta.toUpperCase()));

        TipoDeChavePix tipo = TipoDeChavePix.valueOf(columns[4].toUpperCase());
        String valorChave = columns[5];
        String dataCriacao = columns[6];
        Status status = Status.valueOf(columns[7].toUpperCase());

        return ChavePix.fromMemento(new ChavePix.Memento(tipo, valorChave, dadosBancarios, LocalDateTime.parse(dataCriacao), status));
    }

}
