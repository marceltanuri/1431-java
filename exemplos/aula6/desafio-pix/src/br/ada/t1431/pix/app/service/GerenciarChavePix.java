package br.ada.t1431.pix.app.service;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.ChavePixRepository;
import br.ada.t1431.pix.domain.TipoDeChavePix;
import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.dadosBancarios.TipoDeContaBancaria;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento das chaves Pix.
 * Permite cadastrar, ativar, inativar e expirar chaves Pix.
 */
public class GerenciarChavePix {

    /**
     * Repositório de chaves Pix.
     */
    ChavePixRepository repository;

    /**
     * Construtor do serviço.
     * @param repository repositório de chaves Pix
     */
    public GerenciarChavePix(ChavePixRepository repository) {
        this.repository = repository;
    }

    /**
     * Cadastra uma nova chave Pix.
     * @param valor valor da chave
     * @param tipoDaChave tipo da chave
     * @param instituicao instituição bancária
     * @param agencia agência bancária
     * @param conta número da conta
     * @param tipoDeConta tipo de conta bancária
     * @return chave Pix cadastrada
     */
    public ChavePix cadastrarNova(String valor, String tipoDaChave, String instituicao, String agencia, String conta, String tipoDeConta) {
        DadosBancarios dadosBancarios = new DadosBancarios(instituicao, agencia, conta, TipoDeContaBancaria.valueOf(tipoDeConta.toUpperCase()));
        TipoDeChavePix tipoDeChavePix = TipoDeChavePix.valueOf(tipoDaChave.toUpperCase());

        ChavePix chavePix = tipoDeChavePix == TipoDeChavePix.ALEATORIA ? ChavePix.createChaveAleatoriaAtiva(dadosBancarios) : ChavePix.createChaveAtiva(tipoDeChavePix, valor, dadosBancarios);

        return repository.insert(chavePix);
    }

    /**
     * Cadastra uma nova chave Pix com data de expiração.
     * @param valor valor da chave
     * @param tipoDaChave tipo da chave
     * @param instituicao instituição bancária
     * @param agencia agência bancária
     * @param conta número da conta
     * @param tipoDeConta tipo de conta bancária
     * @param validadeEmMeses validade em meses
     * @return chave Pix cadastrada
     */
    public ChavePix cadastrarNovaComDataDeExpiracao(String valor, String tipoDaChave, String instituicao, String agencia, String conta, String tipoDeConta, int validadeEmMeses) {
        DadosBancarios dadosBancarios = new DadosBancarios(instituicao, agencia, conta, TipoDeContaBancaria.valueOf(tipoDeConta.toUpperCase()));
        TipoDeChavePix tipoDeChavePix = TipoDeChavePix.valueOf(tipoDaChave.toUpperCase());

        ChavePix chavePix = tipoDeChavePix == TipoDeChavePix.ALEATORIA ? ChavePix.createChaveAleatoriaAtiva(dadosBancarios) : ChavePix.createChaveAtivaComExpiracaoEmMeses(tipoDeChavePix, valor, dadosBancarios, validadeEmMeses);

        return repository.insert(chavePix);
    }

    /**
     * Inativa uma chave Pix.
     * @param tipoDaChave tipo da chave
     * @param valor valor da chave
     */
    public void inativar(String tipoDaChave, String valor) {
        Optional<ChavePix> chavePix = repository.find(TipoDeChavePix.valueOf(tipoDaChave.toUpperCase()), valor);
        if (chavePix.isPresent()) {
            chavePix.get().inativar();
            repository.update(chavePix.get());
        } else {
            throw new RuntimeException("Chave não encontrada");
        }
    }

    /**
     * Ativa uma chave Pix.
     * @param tipoDaChave tipo da chave
     * @param valor valor da chave
     */
    public void ativar(String tipoDaChave, String valor) {
        Optional<ChavePix> chavePix = repository.find(TipoDeChavePix.valueOf(tipoDaChave.toUpperCase()), valor);
        if (chavePix.isPresent()) {
            chavePix.get().ativar();
            repository.update(chavePix.get());
        } else {
            throw new RuntimeException("Chave não encontrada");
        }
    }

    /**
     * Expira uma chave Pix na data informada.
     * @param tipoDaChave tipo da chave
     * @param valor valor da chave
     * @param dataExpiracao data de expiração
     */
    public void expirar(String tipoDaChave, String valor, LocalDateTime dataExpiracao) {
        Optional<ChavePix> chavePix = repository.find(TipoDeChavePix.valueOf(tipoDaChave.toUpperCase()), valor);
        if (chavePix.isPresent()) {
            chavePix.get().expirar(dataExpiracao);
            repository.update(chavePix.get());
        } else {
            throw new RuntimeException("Chave não encontrada");
        }
    }

    /**
     * Busca uma chave Pix.
     * @param tipoDaChave tipo da chave
     * @param valor valor da chave
     * @return chave Pix encontrada
     */
    public Optional<ChavePix> buscar(String tipoDaChave, String valor) {
        return repository.find(TipoDeChavePix.valueOf(tipoDaChave.toUpperCase()), valor);
    }

}