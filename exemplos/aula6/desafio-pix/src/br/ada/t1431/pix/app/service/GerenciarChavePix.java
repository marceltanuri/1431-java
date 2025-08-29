package br.ada.t1431.pix.app.service;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.ChavePixRepository;
import br.ada.t1431.pix.domain.TipoDeChavePix;
import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;
import br.ada.t1431.pix.domain.dadosBancarios.TipoDeContaBancaria;

import java.util.Optional;

public class GerenciarChavePix {

    ChavePixRepository repository;

    public GerenciarChavePix(ChavePixRepository repository) {
        this.repository = repository;
    }

    public ChavePix salvar(String valor, String tipoDaChave, String instituicao, String agencia, String conta, String tipoDeConta) {
        DadosBancarios dadosBancarios = new DadosBancarios(instituicao, agencia, conta, TipoDeContaBancaria.valueOf(tipoDeConta.toUpperCase()));
        TipoDeChavePix tipoDeChavePix = TipoDeChavePix.valueOf(tipoDaChave.toUpperCase());

        ChavePix chavePix = tipoDeChavePix == TipoDeChavePix.ALEATORIA ? ChavePix.createChaveAleatoriaAtiva(dadosBancarios) : ChavePix.createChaveAtiva(tipoDeChavePix, valor, dadosBancarios);

        return repository.insert(chavePix);
    }

    public void inativar(String tipoDaChave, String valor) {
        Optional<ChavePix> chavePix = repository.find(TipoDeChavePix.valueOf(tipoDaChave.toUpperCase()), valor);
        if (chavePix.isPresent()) {
            chavePix.get().inativar();
            repository.update(chavePix.get());
        } else {
            throw new RuntimeException("Chave não encontrada");
        }
    }

    public void ativar(String tipoDaChave, String valor) {
        Optional<ChavePix> chavePix = repository.find(TipoDeChavePix.valueOf(tipoDaChave.toUpperCase()), valor);
        if (chavePix.isPresent()) {
            chavePix.get().ativar();
            repository.update(chavePix.get());
        } else {
            throw new RuntimeException("Chave não encontrada");
        }
    }

    public Optional<ChavePix> buscar(String tipoDaChave, String valor) {
        return repository.find(TipoDeChavePix.valueOf(tipoDaChave.toUpperCase()), valor);
    }

}