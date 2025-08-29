package br.ada.t1431.pix.app.controller.command.impl;

import br.ada.t1431.pix.app.controller.command.Command;
import br.ada.t1431.pix.app.service.GerenciarChavePix;

import java.util.Map;

public class InativarCommand implements Command {

    private GerenciarChavePix service;
    private Map<String, String> params;

    public InativarCommand(GerenciarChavePix service, Map<String, String> mapaDeParametros) {
        this.params = mapaDeParametros;
        this.service = service;
    }

    @Override
    public void execute() {
        String tipoDaChave = params.get("t");
        String valorDaChave = params.get("v");
        try {
            service.inativar(tipoDaChave, valorDaChave);
            System.out.println("Chave inativada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao inativar a chave." + e.getMessage());
        }
    }
}
