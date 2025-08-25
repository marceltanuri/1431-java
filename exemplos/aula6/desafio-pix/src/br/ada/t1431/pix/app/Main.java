package br.ada.t1431.pix.app;

import br.ada.t1431.pix.domain.ChavePixRepository;
import br.ada.t1431.pix.app.controller.AppCLIController;
import br.ada.t1431.pix.app.repository.ArquivoLocalRepository;
import br.ada.t1431.pix.app.service.GerenciarChavePix;

public class Main {
    public static void main(String[] args) {
        ChavePixRepository repository = new ArquivoLocalRepository("");
        GerenciarChavePix service = new GerenciarChavePix(repository);
        AppCLIController controller = new AppCLIController(service);
        controller.processar(args);
    }
}
