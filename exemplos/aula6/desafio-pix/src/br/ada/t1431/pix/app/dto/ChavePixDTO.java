package br.ada.t1431.pix.app.dto;

import br.ada.t1431.pix.domain.ChavePix;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public record ChavePixDTO(String codigoInstituicao, String agencia, String conta, String tipoConta, String tipoChave,
                          String valorChave, String dataCriacao, String status, String expirationInfo) {


    public static ChavePixDTO from(ChavePix chavePix) {
        String expirationInfo = getExpirationInfo(chavePix);

        String dataCriacaoFormatted = chavePix.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        return new ChavePixDTO(chavePix.getDadosBancarios().codigoBanco(), chavePix.getDadosBancarios().agencia(), chavePix.getDadosBancarios().conta(), chavePix.getDadosBancarios().tipoDeContaBancaria().name(), chavePix.getTipo().name(), chavePix.getValor(), dataCriacaoFormatted, chavePix.getStatus().name(), expirationInfo);
    }

    private static String getExpirationInfo(ChavePix chavePix) {

        if (chavePix.getDataExpiracao() == null) {
            return "Não expira";
        }

        long between = ChronoUnit.DAYS.between(chavePix.getDataExpiracao().toLocalDate(), LocalDate.now());
        return chavePix.isExpired() ? "Expirada há " + between + " dias" : "Expira em " + between * -1 + " dias";
    }

}
