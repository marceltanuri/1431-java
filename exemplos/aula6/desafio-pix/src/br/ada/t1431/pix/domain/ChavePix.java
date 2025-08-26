package br.ada.t1431.pix.domain;

import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.validador.Validador;

// 4 principios da POO
// Abstração, Encapsulamento, Herança, Polimorfismo
public class ChavePix {

    private final TipoDeChavePix tipo;
    private final String valor;
    private final DadosBancarios dadosBancarios;
    private final Validador validador;

    ChavePix(TipoDeChavePix tipo, String valor, DadosBancarios dadosBancarios, Validador validador) {
        this.tipo = tipo;
        this.valor = valor;
        this.dadosBancarios = dadosBancarios;
        this.validador = validador;
        validar();
    }

    private void validar(){
        validador.validar(this.valor);
    }

    public TipoDeChavePix getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public DadosBancarios getDadosBancarios() {
        return dadosBancarios;
    }
}
