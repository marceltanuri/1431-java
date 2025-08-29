package br.ada.t1431.pix.app.dto;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.ChavePixFactory;
import br.ada.t1431.pix.domain.TipoDeChavePix;
import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.dadosBancarios.TipoDeContaBancaria;

import java.time.format.DateTimeFormatter;

public record ChavePixDTO(String codigoInstituicao, String agencia, String conta, String tipoConta, String tipoChave, String valorChave, String dataCriacao, String status, boolean isExpired) {

    public ChavePix toChavePix(){
        return ChavePixFactory.create(TipoDeChavePix.valueOf(tipoChave.toUpperCase()), valorChave, new DadosBancarios(agencia, conta, codigoInstituicao, TipoDeContaBancaria.valueOf(tipoConta().toUpperCase())));
    }

    public static ChavePixDTO from(ChavePix chavePix){
        return new ChavePixDTO(chavePix.getDadosBancarios().codigoBanco(), chavePix.getDadosBancarios().agencia(), chavePix.getDadosBancarios().conta(), chavePix.getDadosBancarios().tipoDeContaBancaria().name(), chavePix.getTipo().name(), chavePix.getValor(), chavePix.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), chavePix.getStatus().name(), chavePix.isExpired());
    }

}
