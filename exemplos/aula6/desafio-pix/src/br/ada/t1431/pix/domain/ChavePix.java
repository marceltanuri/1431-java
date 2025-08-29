package br.ada.t1431.pix.domain;

import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.validador.Validador;
import br.ada.t1431.pix.domain.validador.impl.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChavePix {

    private TipoDeChavePix tipo;
    private String valor;
    private DadosBancarios dadosBancarios;
    private LocalDateTime dataCriacao;
    private Status status;
    private LocalDateTime dataExpiracao;

    private ChavePix() {

    }

    private ChavePix(TipoDeChavePix tipo, String valor, DadosBancarios dadosBancarios, LocalDateTime dataCriacao, Status status) {
        this.tipo = tipo;
        this.valor = valor;
        this.dadosBancarios = dadosBancarios;
        this.dataCriacao = dataCriacao;
        this.status = status;
    }

    public static ChavePix createChaveAtiva(final TipoDeChavePix tipo, final String valor, final DadosBancarios dadosBancarios) {
        ChavePix chavePix = new ChavePix(tipo, valor, dadosBancarios, LocalDateTime.now(), Status.ATIVO);
        chavePix.validar();
        return chavePix;
    }

    public static ChavePix createChaveAtivaComExpiracaoEmMeses(final TipoDeChavePix tipo, final String valor, final DadosBancarios dadosBancarios, int meses) {
        ChavePix chavePix = new ChavePix(tipo, valor, dadosBancarios, LocalDateTime.now(), Status.ATIVO);
        chavePix.expirar(meses);
        chavePix.validar();
        return chavePix;
    }

    public static ChavePix createChaveAleatoriaAtiva(final DadosBancarios dadosBancarios) {
        return createChaveAtiva(TipoDeChavePix.ALEATORIA, UUID.randomUUID().toString(), dadosBancarios);
    }

    public static ChavePix createChaveAleatoriaAtivaComExpiracaoEmMeses(final DadosBancarios dadosBancarios, int meses) {
        ChavePix chaveAtiva = createChaveAtiva(TipoDeChavePix.ALEATORIA, UUID.randomUUID().toString(), dadosBancarios);
        chaveAtiva.expirar(meses);
        return chaveAtiva;
    }

    /**
     * Reconstitui a entidade a partir de um snapshot, **sem revalidar**.
     * Usa um Validador no-op apenas para satisfazer o construtor atual.
     */
    public static ChavePix fromMemento(Memento m) {
        return new ChavePix(m.tipo(), m.valor(), m.dadosBancarios(), m.dataCriacao(), m.status());
    }

    public void expirar(int meses) {
        this.dataExpiracao = this.dataCriacao.plusMonths(meses);
    }

    private void validar() {
        final Validador validador = resolveValidador();
        validador.validar(this.valor);
    }

    private Validador resolveValidador() {
        return switch (tipo) {
            case CPF -> new ValidadorCPF();
            case CNPJ -> new ValidadorCNPJ();
            case EMAIL -> new ValidadorEmail();
            case CELULAR -> new ValidadorCelular();
            case ALEATORIA -> new ValidadorAleatoria();
        };
    }

    public boolean isExpired() {
        if (dataExpiracao != null) return LocalDateTime.now().isAfter(dataExpiracao);
        return false;
    }

    public void inativar() {
        this.status = Status.INATIVO;
    }

    public void ativar() {
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

    public record Memento(TipoDeChavePix tipo, String valor, DadosBancarios dadosBancarios, LocalDateTime dataCriacao,
                          Status status) {
    }
}
