package br.ada.t1431.pix.app.controller;

import br.ada.t1431.pix.app.controller.command.Command;
import br.ada.t1431.pix.app.controller.command.CommandFactory;
import br.ada.t1431.pix.app.service.GerenciarChavePix;

import java.util.HashMap;
import java.util.Map;

public class AppCLIController {

    private final Map<String, String> mapaDeParametros = new HashMap<>();
    private final GerenciarChavePix gerenciarChavePix;

    public AppCLIController(GerenciarChavePix gerenciarChavePix) {
        this.gerenciarChavePix = gerenciarChavePix;
    }

    public void processar(String[] parametros) {
        for (int i = 0; i < parametros.length - 1; i++) {
            mapaDeParametros.put(parametros[i].replace("-", ""), parametros[i + 1]);
        }

        Command command = CommandFactory.getInstance().create(mapaDeParametros.get("cmd"), mapaDeParametros, gerenciarChavePix);

        command.execute();
    }

    // -i: instituicao
    // -cn: numero da conta
    // -a: agencia
    // -ct: tipoDeContaBancaria de conta
    // -t: tipoDeContaBancaria da chave
    // -v: valor da chave
    // -cmd comando

    // java Main -a asaas -b sadsds -c dfddfd -d sdsdsfs

    // Main (entrada dos parametros) -> Controller -> Service -> Usar o dominio (modelo) e orquestar (se necessario) com o Repository -> Repository

}
