package br.ada.t1431.pix.app.controller.command.impl;

import br.ada.t1431.pix.app.controller.command.Command;
import br.ada.t1431.pix.app.service.GerenciarChavePix;

import java.util.Map;

public class CadastrarCommand implements Command {

    private GerenciarChavePix service;
    private Map<String, String> params;

    public CadastrarCommand(GerenciarChavePix service, Map<String, String> mapaDeParametros) {
        this.params = mapaDeParametros;
        this.service = service;
    }

    @Override
    public void execute() {
        String instituicao = params.get("i");
        String agencia = params.get("a");
        String numeroDaConta = params.get("c");
        String tipoDaConta = params.get("tc");
        String tipoDaChave = params.get("t");
        String valorDaChave = params.get("v");
        try {
            service.salvar(valorDaChave, tipoDaChave, instituicao, agencia, numeroDaConta, tipoDaConta);
            System.out.println("Chave cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar a chave. " + e.getMessage());
            e.printStackTrace();
        }
    }
}
