package br.ada.t1431.pix.domain;

import java.util.Optional;

public interface ChavePixRepository {
    ChavePix save(ChavePix chavePix);
    void delete(TipoDeChavePix tipo, String valor);
    Optional<ChavePix> find(TipoDeChavePix tipo, String valor);
}