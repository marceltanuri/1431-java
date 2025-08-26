package br.ada.t1431.pix.app.controller.command;

import br.ada.t1431.pix.app.controller.command.impl.BuscarCommand;
import br.ada.t1431.pix.app.controller.command.impl.CadastrarCommand;
import br.ada.t1431.pix.app.controller.command.impl.RemoverCommand;
import br.ada.t1431.pix.app.service.GerenciarChavePix;

import java.util.Map;

public class CommandFactory {

    private static CommandFactory instance;

    private CommandFactory(){

    }

    public static CommandFactory getInstance(){
        if(instance == null){
            instance = new CommandFactory();
        }
        return instance;
    }

    public Command create(String cmd, Map<String, String> params, GerenciarChavePix service) {
        if(cmd==null){
            throw new IllegalArgumentException("cmd não pode ser nulo");
        }
        return switch (cmd) {
            case "cadastrar" -> new CadastrarCommand(service, params);
            case "buscar" -> new BuscarCommand(service, params);
            case "remover" -> new RemoverCommand(service, params);
            default -> throw new IllegalStateException("Unexpected value: " + cmd);
        };
    }

}
