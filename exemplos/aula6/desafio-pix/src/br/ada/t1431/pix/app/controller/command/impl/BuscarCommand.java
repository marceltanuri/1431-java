package br.ada.t1431.pix.app.controller.command.impl;

import br.ada.t1431.pix.app.controller.command.Command;
import br.ada.t1431.pix.app.dto.ChavePixDTO;
import br.ada.t1431.pix.app.service.GerenciarChavePix;
import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.exception.ChaveNaoEncontradaException;

import java.util.Map;

public class BuscarCommand implements Command {

    private GerenciarChavePix service;
    private Map<String, String> params;

    public BuscarCommand(GerenciarChavePix service, Map<String, String> mapaDeParametros) {
        this.params = mapaDeParametros;
        this.service = service;
    }

    @Override
    public void execute() {
        String valorDaChave = params.get("v");
        String tipoDaChave = params.get("t");
        try {
            ChavePix chavePix = service.buscar(tipoDaChave, valorDaChave).orElseThrow(ChaveNaoEncontradaException::new);
            System.out.println("Chave encontrada!");
            System.out.println(ChavePixDTO.from(chavePix));
        } catch (Exception e) {
            System.out.println("Erro ao buscar a chave. " + e.getMessage());
            e.printStackTrace();
        }
    }
}
