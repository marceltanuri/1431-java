package br.ada.t1431.pix.infrastructure.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOperations {

    private final Path arquivo;
    private final String nomeArquivo;

    public FileOperations(String diretorio, String nomeArquivo) {
        Path pathDiretorio = Paths.get(diretorio);
        try {
            Files.createDirectories(pathDiretorio);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório para o arquivo " + nomeArquivo , e);
        }
        this.nomeArquivo = nomeArquivo;
        this.arquivo = pathDiretorio.resolve(nomeArquivo);
    }

    /**
     * Lê todas as linhas do arquivo associado a esta instância.
     * Se o arquivo não existir, retorna uma lista vazia.
     * @return Uma lista de strings, onde cada string é uma linha do arquivo.
     */
    public List<String> lerLinhas() {
        if (!Files.exists(arquivo)) {
            return new ArrayList<>();
        }
        try (Stream<String> lines = Files.lines(arquivo)) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo " + nomeArquivo, e);
        }
    }

    /**
     * Escreve uma lista de strings no arquivo, sobrescrevendo o conteúdo existente.
     * Cada string na lista será escrita como uma nova linha no arquivo.
     * @param linhas A lista de strings a serem escritas no arquivo.
     */
    public void escreverLinhas(List<String> linhas) {
        try {
            Files.write(arquivo, linhas);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo " + nomeArquivo, e);
        }
    }
}
