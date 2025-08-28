package br.ada.t1431.pix.infrastructure.util;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.ChavePixFactory;
import br.ada.t1431.pix.infrastructure.repository.ChavePixMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ChavePixArquivoLocalDAO {

    private static final String SEPARADOR = ";";
    private final FileOperationsUtil fileOperations;

    public ChavePixArquivoLocalDAO(String diretorio, String nomeArquivo) {
        this.fileOperations = new FileOperationsUtil(diretorio, nomeArquivo);
    }

    public void escreverChaves(List<ChavePix> chaves) {
        List<String> linhas = chaves.stream()
                .map(this::formatarLinha)
                .collect(Collectors.toList());
        fileOperations.escreverLinhas(linhas);
    }

    public List<ChavePix> lerTodasAsChaves() {
        return fileOperations.lerLinhas().stream()
                .map(this::parseLinha)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private ChavePix parseLinha(String linha) {
        final int NUMERO_DE_PARTES_ESPERADO = 6;
        String[] partes = linha.split(SEPARADOR, NUMERO_DE_PARTES_ESPERADO);
        if (partes.length < NUMERO_DE_PARTES_ESPERADO) {
            System.err.println("Linha mal formatada no arquivo de chaves, será ignorada: " + linha);
            return null;
        }
        return ChavePixMapper.from(partes);
    }

    private String formatarLinha(ChavePix chave) {
        return String.join(SEPARADOR,
                chave.getDadosBancarios().codigoBanco(),
                chave.getDadosBancarios().agencia(),
                chave.getDadosBancarios().conta(),
                chave.getDadosBancarios().tipoDeContaBancaria().name(),
                chave.getTipo().name(),
                chave.getValor());
    }

}
