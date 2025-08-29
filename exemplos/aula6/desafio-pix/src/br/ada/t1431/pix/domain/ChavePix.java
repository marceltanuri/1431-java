package br.ada.t1431.pix.domain;

import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.validador.Validador;

import java.time.LocalDateTime;

// 4 principios da POO
// Abstração, Encapsulamento, Herança, Polimorfismo
public class ChavePix {

    private final TipoDeChavePix tipo;
    private final String valor;
    private final DadosBancarios dadosBancarios;
    private final LocalDateTime dataCriacao;
    private Status status;
    private LocalDateTime dataExpiracao;

    private final Validador validador;

    ChavePix(TipoDeChavePix tipo, String valor, DadosBancarios dadosBancarios, LocalDateTime dataCriacao, Status status, Validador validador) {
        this.tipo = tipo;
        this.valor = valor;
        this.dadosBancarios = dadosBancarios;
        this.validador = validador;
        this.dataCriacao = dataCriacao;
        this.status = status;
        validar();
    }

    /**
     * Reconstitui a entidade a partir de um snapshot, **sem revalidar**.
     * Usa um Validador no-op apenas para satisfazer o construtor atual.
     */
    public static ChavePix fromMemento(Memento m) {
        return new ChavePix(m.tipo(), m.valor(), m.dadosBancarios(), m.dataCriacao(), m.status(),  NoOpValidador.INSTANCE);
    }

    private void validar() {
        validador.validar(this.valor);
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(dataExpiracao);
    }

    public void inativar(){
        this.status = Status.INATIVO;
    }

    public void ativar(){
        this.status = Status.ATIVO;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Exporta o estado persistível da entidade
     */
    public Memento toMemento() {
        return new Memento(this.tipo, this.valor, this.dadosBancarios, this.dataCriacao, this.status);
    }

    /* ============================
       MEMENTO / SNAPSHOT (DDD-friendly)
       ============================ */
    public record Memento(TipoDeChavePix tipo, String valor, DadosBancarios dadosBancarios, LocalDateTime dataCriacao, Status status) {
    }

    /**
     * Validador no-op (não persiste; apenas evita validação na reconstituição)
     */
    private static final class NoOpValidador implements Validador {
        private static final NoOpValidador INSTANCE = new NoOpValidador();

        private NoOpValidador() {
        }

        @Override
        public void validar(String valor) {
            // intencionalmente vazio: reconstituição confia no estado persistido
        }
    }
}
