package br.ada.t1431.pix.infrastructure.repository;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.ChavePixRepository;
import br.ada.t1431.pix.domain.TipoDeChavePix;
import br.ada.t1431.pix.domain.exception.ChaveDuplicadaException;
import br.ada.t1431.pix.infrastructure.util.ChavePixArquivoLocalDAO;

import java.util.List;
import java.util.Optional;

/**
 * Implementação de repositório de chaves Pix usando arquivos locais.
 * Realiza operações de inserção, atualização e busca.
 */
public class ArquivoLocalRepository implements ChavePixRepository {
    private final ChavePixArquivoLocalDAO chavePixArquivoLocalDAO;

    /**
     * Cria uma instância do repositório para um diretório específico.
     * @param diretorio diretório onde o arquivo de chaves será salvo
     */
    public ArquivoLocalRepository(String diretorio) {
        this.chavePixArquivoLocalDAO = new ChavePixArquivoLocalDAO(diretorio, ".chaves");
    }

    /**
     * Insere uma nova chave Pix.
     * @param chavePix chave Pix a ser inserida
     * @return chave Pix inserida
     */
    @Override
    public ChavePix insert(ChavePix chavePix) {
        List<ChavePix> chaves = chavePixArquivoLocalDAO.lerTodasAsChaves();
        boolean chaveJaExistente = chaves.stream().anyMatch(chave -> chave.getValor().equals(chavePix.getValor()) && chave.getTipo().name().equalsIgnoreCase(chavePix.getTipo().name()));

        if (chaveJaExistente) {
            throw new ChaveDuplicadaException("Não foi possível salvar a chave, uma chave com esse mesmo valor e tipo já existe no arquivo de chaves");
        }

        chaves.add(chavePix);
        chavePixArquivoLocalDAO.escreverChaves(chaves);
        return chavePix;
    }

    /**
     * Atualiza uma chave Pix existente.
     * @param chavePix chave Pix a ser atualizada
     * @return chave Pix atualizada
     */
    @Override
    public ChavePix update(ChavePix chavePix) {
        List<ChavePix> chaves = chavePixArquivoLocalDAO.lerTodasAsChaves();
        boolean chaveJaExistente = chaves.stream().anyMatch(chave -> chave.getValor().equals(chavePix.getValor()) && chave.getTipo().name().equalsIgnoreCase(chavePix.getTipo().name()));

        if (!chaveJaExistente) {
            throw new RuntimeException("Chave não encontrada");
        }

        chaves.removeIf(chave -> chave.getValor().equals(chavePix.getValor()) && chave.getTipo().name().equalsIgnoreCase(chavePix.getTipo().name()));

        chaves.add(chavePix);
        chavePixArquivoLocalDAO.escreverChaves(chaves);
        return chavePix;
    }

    /**
     * Busca uma chave Pix pelo tipo e valor.
     * @param tipo tipo da chave
     * @param valor valor da chave
     * @return chave Pix encontrada, se existir
     */
    @Override
    public Optional<ChavePix> find(TipoDeChavePix tipo, String valor) {

        return chavePixArquivoLocalDAO.lerTodasAsChaves().stream()
                .filter(chave -> chave.getValor().equals(valor) && chave.getTipo().name().equalsIgnoreCase(tipo.name()))
                .findFirst();
    }


}
