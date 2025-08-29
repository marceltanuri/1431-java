package br.ada.t1431.pix.domain;

import java.util.Optional;

/**
 * Interface para repositórios de chaves Pix.
 * Define operações de inserção, atualização e busca.
 */
public interface ChavePixRepository {
    /**
     * Insere uma nova chave Pix.
     * @param chavePix chave Pix a ser inserida
     * @return chave Pix inserida
     */
    ChavePix insert(ChavePix chavePix);
    /**
     * Atualiza uma chave Pix existente.
     * @param chavePix chave Pix a ser atualizada
     * @return chave Pix atualizada
     */
    ChavePix update(ChavePix chavePix);
    /**
     * Busca uma chave Pix pelo tipo e valor.
     * @param tipo tipo da chave
     * @param valor valor da chave
     * @return chave Pix encontrada, se existir
     */
    Optional<ChavePix> find(TipoDeChavePix tipo, String valor);
}