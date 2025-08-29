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
        final String instituicao = params.get("i");
        final String agencia = params.get("a");
        final String numeroDaConta = params.get("c");
        final String tipoDaConta = params.get("tc");
        final String tipoDaChave = params.get("t");
        final String valorDaChave = params.get("v");
        final String validadeEmMeses = params.get("m");
        try {
            if (validadeEmMeses == null) {
                service.cadastrarNova(valorDaChave, tipoDaChave, instituicao, agencia, numeroDaConta, tipoDaConta);
            } else {
                service.cadastrarNovaComDataDeExpiracao(valorDaChave, tipoDaChave, instituicao, agencia, numeroDaConta, tipoDaConta, Integer.parseInt(validadeEmMeses));
            }
            System.out.println("Chave cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar a chave. " + e.getMessage());
            e.printStackTrace();
        }
    }
}
