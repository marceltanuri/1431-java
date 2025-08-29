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

        // TODO
        // Ter uma data+hora quando a chave foi criada: a chave é automaticamente gerada pelo sistema: java.date
        // Ao invés de excluir, as chaves serão inativadas (status: ativo, inativo)
        // extra: data de expiração da chave (se data atual maior que data de expiração exibir que a chave está expirada)
        //

    }
}

// Main -> Controler(params) -> Service -> Modelo -> Repositorio
//              ^---------------   ^----------------------|
