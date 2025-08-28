package br.ada.t1431.pix.infrastructure.repository;

import br.ada.t1431.pix.app.dto.ChavePixDTO;
import br.ada.t1431.pix.domain.exception.ChaveDuplicadaException;
import br.ada.t1431.pix.infrastructure.util.FileOperations;
import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.ChavePixRepository;
import br.ada.t1431.pix.domain.TipoDeChavePix;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArquivoLocalRepository implements ChavePixRepository {


    private static final String SEPARADOR = ";";
    private final FileOperations fileOperations;

    public ArquivoLocalRepository(String diretorio) {
        this.fileOperations = new FileOperations(diretorio, ".chaves");
    }

    @Override
    public ChavePix save(ChavePix chavePix) {
        List<ChavePixDTO> chaves = lerTodasAsChaves();
        boolean chaveJaExistente = chaves.stream().anyMatch(chave -> chave.valorChave().equals(chavePix.getValor()) && chave.tipoChave().equals(chavePix.getTipo().name()));

        if(chaveJaExistente){
            throw new ChaveDuplicadaException("Não foi possível salvar a chave, uma chave com esse mesmo valor e tipo já existe no arquivo de chaves");
        }

        chaves.add(ChavePixDTO.from(chavePix));
        escreverChaves(chaves);
        return chavePix;
    }


    @Override
    public void delete(TipoDeChavePix tipo, String valor) {
        List<ChavePixDTO> chaves = lerTodasAsChaves();
        boolean foiRemovido = chaves.removeIf(chave -> chave.valorChave().equals(valor) && chave.tipoChave().equals(tipo.name()));

        if (!foiRemovido) {
            return;
        }

        escreverChaves(chaves);
    }

    @Override
    public Optional<ChavePix> find(TipoDeChavePix tipo, String valor) {

        Optional<ChavePixDTO> first = lerTodasAsChaves().stream()
                .filter(chave -> chave.valorChave().equals(valor) && chave.tipoChave().equals(tipo.name()))
                .findFirst();

        return first.map(ChavePixDTO::toChavePix);
    }

    private void escreverChaves(List<ChavePixDTO> chaves) {
        List<String> linhas = chaves.stream()
                .map(this::formatarLinha)
                .collect(Collectors.toList());
        fileOperations.escreverLinhas(linhas);
    }

    private List<ChavePixDTO> lerTodasAsChaves() {
        return fileOperations.lerLinhas().stream()
                .map(this::parseLinha)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private ChavePixDTO parseLinha(String linha) {
        final int NUMERO_DE_PARTES_ESPERADO = 6;
        String[] partes = linha.split(SEPARADOR, NUMERO_DE_PARTES_ESPERADO);
        if (partes.length < NUMERO_DE_PARTES_ESPERADO) {
            System.err.println("Linha mal formatada no arquivo de chaves, será ignorada: " + linha);
            return null;
        }
        return new ChavePixDTO(partes[0], partes[1], partes[2], partes[3], partes[4], partes[5]);
    }

    private String formatarLinha(ChavePixDTO chave) {
        return String.join(SEPARADOR,
                chave.codigoInstituicao(),
                chave.agencia(),
                chave.conta(),
                chave.tipoConta(),
                chave.tipoChave(),
                chave.valorChave());
    }
}
