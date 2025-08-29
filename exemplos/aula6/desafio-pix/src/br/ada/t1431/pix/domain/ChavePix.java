package br.ada.t1431.pix.domain;

import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.validador.Validador;
import br.ada.t1431.pix.domain.validador.impl.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa uma chave Pix.
 * Contém informações sobre tipo, valor, dados bancários, status, datas de criação e expiração.
 */
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

    private ChavePix(TipoDeChavePix tipo, String valor, DadosBancarios dadosBancarios, LocalDateTime dataCriacao, Status status, LocalDateTime dataExpiracao) {
        this.tipo = tipo;
        this.valor = valor;
        this.dadosBancarios = dadosBancarios;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.dataExpiracao = dataExpiracao;
    }

    /**
     * Cria uma chave Pix ativa.
     * @param tipo tipo da chave
     * @param valor valor da chave
     * @param dadosBancarios dados bancários associados
     * @return chave Pix criada
     */
    public static ChavePix createChaveAtiva(final TipoDeChavePix tipo, final String valor, final DadosBancarios dadosBancarios) {
        ChavePix chavePix = new ChavePix(tipo, valor, dadosBancarios, LocalDateTime.now(), Status.ATIVO);
        chavePix.validar();
        return chavePix;
    }

    /**
     * Cria uma chave Pix ativa com expiração em meses.
     * @param tipo tipo da chave
     * @param valor valor da chave
     * @param dadosBancarios dados bancários associados
     * @param meses quantidade de meses para expiração
     * @return chave Pix criada
     */
    public static ChavePix createChaveAtivaComExpiracaoEmMeses(final TipoDeChavePix tipo, final String valor, final DadosBancarios dadosBancarios, int meses) {
        ChavePix chavePix = new ChavePix(tipo, valor, dadosBancarios, LocalDateTime.now(), Status.ATIVO);
        chavePix.expirar(meses);
        chavePix.validar();
        return chavePix;
    }

    /**
     * Cria uma chave Pix aleatória ativa.
     * @param dadosBancarios dados bancários associados
     * @return chave Pix criada
     */
    public static ChavePix createChaveAleatoriaAtiva(final DadosBancarios dadosBancarios) {
        return createChaveAtiva(TipoDeChavePix.ALEATORIA, UUID.randomUUID().toString(), dadosBancarios);
    }

    /**
     * Cria uma chave Pix aleatória ativa com expiração em meses.
     * @param dadosBancarios dados bancários associados
     * @param meses quantidade de meses para expiração
     * @return chave Pix criada
     */
    public static ChavePix createChaveAleatoriaAtivaComExpiracaoEmMeses(final DadosBancarios dadosBancarios, int meses) {
        ChavePix chaveAtiva = createChaveAtiva(TipoDeChavePix.ALEATORIA, UUID.randomUUID().toString(), dadosBancarios);
        chaveAtiva.expirar(meses);
        return chaveAtiva;
    }

    /**
     * Reconstitui a entidade a partir de um snapshot, sem revalidar.
     * @param memento estado persistido da entidade
     * @return chave Pix reconstituída
     */
    public static ChavePix fromMemento(Memento m) {
        return new ChavePix(m.tipo(), m.valor(), m.dadosBancarios(), m.dataCriacao(), m.status(), m.dataExpiracao);
    }

    /**
     * Define a data de expiração da chave Pix em meses.
     * @param meses quantidade de meses para expiração
     */
    public void expirar(int meses) {
        this.dataExpiracao = this.dataCriacao.plusMonths(meses);
    }

    /**
     * Define a data de expiração da chave Pix.
     * @param dataExpiracao data de expiração
     */
    public void expirar(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
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

    /**
     * Verifica se a chave está expirada.
     * @return true se expirada, false caso contrário
     */
    public boolean isExpired() {
        if (dataExpiracao != null) return LocalDateTime.now().isAfter(dataExpiracao);
        return false;
    }

    /**
     * Inativa a chave Pix.
     */
    public void inativar() {
        this.status = Status.INATIVO;
    }

    /**
     * Ativa a chave Pix.
     */
    public void ativar() {
        this.status = Status.ATIVO;
    }

    /**
     * Retorna o tipo da chave Pix.
     * @return tipo da chave
     */
    public TipoDeChavePix getTipo() {
        return tipo;
    }

    /**
     * Retorna o valor da chave Pix.
     * @return valor da chave
     */
    public String getValor() {
        return valor;
    }

    /**
     * Retorna os dados bancários associados à chave Pix.
     * @return dados bancários
     */
    public DadosBancarios getDadosBancarios() {
        return dadosBancarios;
    }

    /**
     * Retorna a data de criação da chave Pix.
     * @return data de criação
     */
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    /**
     * Retorna o status da chave Pix.
     * @return status da chave
     */
    public Status getStatus() {
        if (isExpired()) return Status.EXPIRADA;
        return status;
    }

    /**
     * Retorna a data de expiração da chave Pix.
     * @return data de expiração
     */
    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    /**
     * Exporta o estado persistível da entidade.
     * @return memento da chave Pix
     */
    public Memento toMemento() {
        return new Memento(this.tipo, this.valor, this.dadosBancarios, this.dataCriacao, this.status, this.dataExpiracao);
    }

    /**
     * Record que representa o estado persistível da chave Pix.
     * @param tipo tipo da chave
     * @param valor valor da chave
     * @param dadosBancarios dados bancários
     * @param dataCriacao data de criação
     * @param status status da chave
     * @param dataExpiracao data de expiração
     */
    public record Memento(TipoDeChavePix tipo, String valor, DadosBancarios dadosBancarios, LocalDateTime dataCriacao,
                          Status status, LocalDateTime dataExpiracao) {
    }
}
