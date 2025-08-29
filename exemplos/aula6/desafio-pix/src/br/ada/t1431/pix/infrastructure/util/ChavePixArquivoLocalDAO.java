package br.ada.t1431.pix.infrastructure.util;

import br.ada.t1431.pix.domain.ChavePix;
import br.ada.t1431.pix.domain.dadosBancarios.DadosBancarios;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * DAO responsável pela persistência e recuperação de chaves Pix em arquivos locais.
 * <p>
 * Esta classe realiza operações de leitura, escrita e parsing dos dados das chaves Pix,
 * utilizando arquivos locais para armazenamento. O formato utilizado para serialização
 * e desserialização é baseado em registros (mementos) e dados bancários, permitindo
 * reconstruir as entidades a partir do arquivo.
 * <p>
 * Principais responsabilidades:
 * <ul>
 *   <li>Salvar uma lista de chaves Pix no arquivo local</li>
 *   <li>Ler todas as chaves Pix do arquivo local</li>
 *   <li>Converter linhas do arquivo em entidades de domínio</li>
 *   <li>Utilizar utilitários para parsing/reflexão e conversão de tipos</li>
 * </ul>
 * <p>
 * O arquivo é manipulado por meio da classe {@link FileOperationsUtil} e o parsing
 * dos dados é realizado com auxílio das classes {@link ParseUtil} e {@link ReflectionUtil}.
 */
public class ChavePixArquivoLocalDAO {

    private static final Pattern OUTER_PATTERN = Pattern.compile("^\\s*Memento\\[(.*)]\\s*$");
    private static final Pattern KV_PATTERN = Pattern.compile("(\\w+)=((?:DadosBancarios\\[[^\\]]*])|[^,]+)(?:,\\s*|$)");
    private static final Pattern DB_INNER_PATTERN = Pattern.compile("^DadosBancarios\\[(.*)]$");
    private static final Pattern DB_KV_PATTERN = Pattern.compile("(\\w+)=([^,\\]]+)(?:,\\s*|$)");
    private final FileOperationsUtil fileOperations;


    /**
     * Construtor do DAO.
     * Inicializa o utilitário de operações de arquivo para persistência das chaves Pix.
     * @param diretorio diretório onde o arquivo será salvo
     * @param nomeArquivo nome do arquivo de chaves
     */
    public ChavePixArquivoLocalDAO(String diretorio, String nomeArquivo) {
        this.fileOperations = new FileOperationsUtil(diretorio, nomeArquivo);
    }


    /**
     * Escreve uma lista de chaves Pix no arquivo local.
     * Cada chave é convertida para o formato memento e serializada em uma linha.
     * @param chaves lista de chaves Pix a serem persistidas
     */
    public void escreverChaves(List<ChavePix> chaves) {
        List<String> linhas = chaves.stream().map(ChavePix::toMemento).map(this::formatarLinha).collect(Collectors.toList());
        fileOperations.escreverLinhas(linhas);
    }


    /**
     * Lê todas as chaves Pix do arquivo local e reconstrói as entidades de domínio.
     * @return lista de chaves Pix recuperadas do arquivo
     */
    public List<ChavePix> lerTodasAsChaves() {
        return fileOperations.lerLinhas().stream().map(this::parseLinha).filter(Objects::nonNull).map(ChavePix::fromMemento).collect(Collectors.toList());
    }

    /**
     * Converte um memento de chave Pix para o formato de linha do arquivo.
     * @param chave memento da chave Pix
     * @return linha serializada para o arquivo
     */
    private String formatarLinha(ChavePix.Memento chave) {
        return chave.toString();
    }

    /**
     * Realiza o parsing de uma linha do arquivo e reconstrói o memento da chave Pix.
     * @param linha linha do arquivo
     * @return memento da chave Pix ou null se inválida
     */
    private ChavePix.Memento parseLinha(String linha) {
        if (linha == null || linha.isBlank()) return null;
        String inside = ParseUtil.extrairConteudo(linha, OUTER_PATTERN);
        if (inside == null) return null;
        Map<String, String> rootKVs = ParseUtil.extrairParesChaveValor(inside, KV_PATTERN);
        Object dadosBancariosObj = obterDadosBancarios(rootKVs);
        Map<String, Object> parsed = montarValoresParseados(rootKVs, dadosBancariosObj);
        try {
            return instantiateMemento(parsed);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Falha ao instanciar ChavePix.Memento: " + ex.getMessage(), ex);
        }
    }

    /**
     * Obtém o objeto DadosBancarios a partir do mapa de valores extraídos da linha.
     * @param rootKVs mapa de pares chave-valor da linha
     * @return objeto DadosBancarios ou null se não existir
     */
    private Object obterDadosBancarios(Map<String, String> rootKVs) {
        if (rootKVs.containsKey("dadosBancarios")) {
            String dbRaw = rootKVs.get("dadosBancarios");
            return parseDadosBancarios(dbRaw);
        }
        return null;
    }

    /**
     * Monta o mapa de valores parseados, incluindo o objeto DadosBancarios.
     * @param rootKVs mapa de pares chave-valor da linha
     * @param dadosBancariosObj objeto DadosBancarios
     * @return mapa de valores parseados para reconstrução do memento
     */
    private Map<String, Object> montarValoresParseados(Map<String, String> rootKVs, Object dadosBancariosObj) {
        Map<String, Object> parsed = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : rootKVs.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            if (k.equals("dadosBancarios")) {
                parsed.put(k, dadosBancariosObj);
            } else {
                parsed.put(k, ParseUtil.normalizeScalar(v));
            }
        }
        return parsed;
    }

    /**
     * Realiza o parsing do bloco DadosBancarios da linha do arquivo.
     * @param raw string representando o bloco DadosBancarios
     * @return objeto DadosBancarios reconstruído
     */
    private Object parseDadosBancarios(String raw) {
        String inside = ParseUtil.extrairConteudo(raw, DB_INNER_PATTERN);
        Map<String, String> kv = ParseUtil.extrairParesChaveValor(inside, DB_KV_PATTERN);
        return construirDadosBancarios(kv);
    }

    /**
     * Constrói o objeto DadosBancarios a partir do mapa de valores extraídos.
     * @param kv mapa de pares chave-valor do bloco DadosBancarios
     * @return objeto DadosBancarios
     */
    private Object construirDadosBancarios(Map<String, String> kv) {
        try {
            Class<?> dbClass = DadosBancarios.class;
            if (!dbClass.isRecord()) {
                Constructor<?> ctor = ReflectionUtil.findCompatibleConstructor(dbClass, new Class<?>[]{String.class, String.class, String.class, Enum.class});
                Object tipoConta = ReflectionUtil.tryParseEnum(ReflectionUtil.findEnumTypeInCtor(ctor, 3), kv.get("tipoDeContaBancaria"));
                return ctor.newInstance(kv.get("agencia"), kv.get("conta"), kv.get("codigoBanco"), tipoConta);
            }
            RecordComponent[] components = dbClass.getRecordComponents();
            Object[] args = new Object[components.length];
            for (int i = 0; i < components.length; i++) {
                RecordComponent rc = components[i];
                String name = rc.getName();
                Class<?> type = rc.getType();
                String value = kv.get(name);
                args[i] = ParseUtil.coerce(type, value);
            }
            Class<?>[] paramTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
            Constructor<?> canonical = dbClass.getDeclaredConstructor(paramTypes);
            canonical.setAccessible(true);
            return canonical.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Erro criando DadosBancarios por reflexão", e);
        }
    }

    /**
     * Instancia o memento da chave Pix via reflexão, utilizando os valores parseados.
     * @param parsed mapa de valores parseados
     * @return memento da chave Pix
     * @throws ReflectiveOperationException caso haja erro na reflexão
     */
    private ChavePix.Memento instantiateMemento(Map<String, Object> parsed) throws ReflectiveOperationException {
        Class<ChavePix.Memento> mClass = ChavePix.Memento.class;
        if (!mClass.isRecord()) {
            throw new IllegalStateException("ChavePix.Memento não é um record.");
        }
        RecordComponent[] components = mClass.getRecordComponents();
        Class<?>[] paramTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new);
        Object[] args = montarArgumentosMemento(components, parsed);
        Constructor<ChavePix.Memento> canonical = mClass.getDeclaredConstructor(paramTypes);
        canonical.setAccessible(true);
        return canonical.newInstance(args);
    }

    /**
     * Monta o array de argumentos para o construtor do memento da chave Pix.
     * @param components componentes do record
     * @param parsed mapa de valores parseados
     * @return array de argumentos para o construtor
     */
    private Object[] montarArgumentosMemento(RecordComponent[] components, Map<String, Object> parsed) {
        Object[] args = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            RecordComponent rc = components[i];
            String name = rc.getName();
            Class<?> type = rc.getType();
            Object val = parsed.get(name);
            args[i] = ajustarTipoArgumento(type, val, name);
        }
        return args;
    }

    /**
     * Ajusta o tipo do argumento para o construtor do memento, realizando conversões necessárias.
     * @param type tipo do campo
     * @param val valor do campo
     * @param name nome do campo
     * @return valor ajustado para o tipo esperado
     */
    private Object ajustarTipoArgumento(Class<?> type, Object val, String name) {
        if (val != null) {
            if (type.isEnum() && val instanceof String s) {
                return ReflectionUtil.tryParseEnum(type, s);
            } else if (type == java.time.LocalDateTime.class && val instanceof String s) {
                return java.time.LocalDateTime.parse(s);
            } else if (!type.isInstance(val)) {
                return ParseUtil.coerce(type, Objects.toString(val, null));
            } else {
                return val;
            }
        } else {
            if (type.isPrimitive()) {
                throw new IllegalArgumentException("Campo '" + name + "' é primitivo e não pode ser null.");
            }
            return null;
        }
    }
}
