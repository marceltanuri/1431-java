package br.ada.t1431.pix.app.controller;

import br.ada.t1431.pix.app.controller.command.Command;
import br.ada.t1431.pix.app.controller.command.CommandFactory;
import br.ada.t1431.pix.app.service.GerenciarChavePix;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller responsável por processar comandos da linha de comando para o sistema Pix.
 */
public class AppCLIController {

    /**
     * Mapa de parâmetros recebidos da linha de comando.
     */
    private final Map<String, String> mapaDeParametros = new HashMap<>();
    /**
     * Serviço de gerenciamento de chaves Pix.
     */
    private final GerenciarChavePix gerenciarChavePix;

    /**
     * Construtor do controller.
     * @param gerenciarChavePix serviço de gerenciamento de chaves Pix
     */
    public AppCLIController(GerenciarChavePix gerenciarChavePix) {
        this.gerenciarChavePix = gerenciarChavePix;
    }

    /**
     * Processa os parâmetros da linha de comando e executa o comando correspondente.
     * @param parametros argumentos da linha de comando
     */
    public void processar(String[] parametros) {
        for (int i = 0; i < parametros.length - 1; i++) {
            mapaDeParametros.put(parametros[i].replace("-", ""), parametros[i + 1]);
        }

        Command command = CommandFactory.getInstance().create(mapaDeParametros.get("cmd"), mapaDeParametros, gerenciarChavePix);

        command.execute();
    }

    // -i: instituicao
    // -c: numero da conta
    // -a: agencia
    // -tc: tipoDeContaBancaria de conta
    // -t: tipoDeContaBancaria da chave
    // -v: valor da chave
    // -cmd comando
}
