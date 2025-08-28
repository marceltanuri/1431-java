package br.ada.t1431.pix.infrastructure.repository;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.ChavePixFactory;
import br.ada.t1431.pix.domain.TipoDeChavePix;
import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.dadosBancarios.TipoDeContaBancaria;

public class ChavePixMapper
{

    public static ChavePix from(String[] columns){

        String codigoInstituicao = columns[0];
        String agencia  = columns[1];
        String conta = columns[2];
        String tipoConta  = columns[3];

        DadosBancarios dadosBancarios = new DadosBancarios(agencia, conta, codigoInstituicao, TipoDeContaBancaria.valueOf(tipoConta.toUpperCase()));

        TipoDeChavePix tipo = TipoDeChavePix.valueOf(columns[4].toUpperCase());
        String valorChave = columns[5];

        ChavePix chave = ChavePixFactory.create(tipo, valorChave, dadosBancarios);

        return chave;
    }

}
