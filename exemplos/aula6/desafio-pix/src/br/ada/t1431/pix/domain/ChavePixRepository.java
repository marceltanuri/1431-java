package br.ada.t1431.pix.domain;

import java.util.Optional;

public interface ChavePixRepository {
    ChavePix insert(ChavePix chavePix);
    ChavePix update(ChavePix chavePix);
    Optional<ChavePix> find(TipoDeChavePix tipo, String valor);
}