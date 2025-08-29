package br.ada.t1431.pix.app.dto;

import br.ada.t1431.pix.domain.ChavePix;

import java.time.format.DateTimeFormatter;

public record ChavePixDTO(String codigoInstituicao, String agencia, String conta, String tipoConta, String tipoChave,
                          String valorChave, String dataCriacao, String status, boolean isExpired) {


    public static ChavePixDTO from(ChavePix chavePix) {
        return new ChavePixDTO(chavePix.getDadosBancarios().codigoBanco(), chavePix.getDadosBancarios().agencia(), chavePix.getDadosBancarios().conta(), chavePix.getDadosBancarios().tipoDeContaBancaria().name(), chavePix.getTipo().name(), chavePix.getValor(), chavePix.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), chavePix.getStatus().name(), chavePix.isExpired());
    }

}
