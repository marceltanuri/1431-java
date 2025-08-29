package br.ada.t1431.pix.app;

import br.ada.t1431.pix.domain.ChavePixRepository;
import br.ada.t1431.pix.app.controller.AppCLIController;
import br.ada.t1431.pix.infrastructure.repository.ArquivoLocalRepository;
import br.ada.t1431.pix.app.service.GerenciarChavePix;

/**
 * Classe principal do sistema Pix.
 * Responsável por inicializar os componentes e iniciar o processamento dos comandos.
 */
public class Main {
    /**
     * Método principal que inicializa o repositório, serviço e controller.
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        ChavePixRepository repository = new ArquivoLocalRepository("chaves_pix_data");
        GerenciarChavePix service = new GerenciarChavePix(repository);
        AppCLIController controller = new AppCLIController(service);
        controller.processar(args);
    }
}
