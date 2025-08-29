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

public class ChavePixArquivoLocalDAO {

    private static final Pattern OUTER_PATTERN = Pattern.compile("^\\s*Memento\\[(.*)]\\s*$");
    private static final Pattern KV_PATTERN = Pattern.compile("(\\w+)=((?:DadosBancarios\\[[^\\]]*])|[^,]+)(?:,\\s*|$)");
    private static final Pattern DB_INNER_PATTERN = Pattern.compile("^DadosBancarios\\[(.*)]$");
    private static final Pattern DB_KV_PATTERN = Pattern.compile("(\\w+)=([^,\\]]+)(?:,\\s*|$)");
    private final FileOperationsUtil fileOperations;

    // ====== PARSE ======

    public ChavePixArquivoLocalDAO(String diretorio, String nomeArquivo) {
        this.fileOperations = new FileOperationsUtil(diretorio, nomeArquivo);
    }

    private static boolean isAssignable(Class<?> target, Class<?> value) {
        if (target.isAssignableFrom(value)) return true;
        if (target.isPrimitive()) {
            return (target == boolean.class && value == Boolean.class) || (target == int.class && value == Integer.class) || (target == long.class && value == Long.class) || (target == double.class && value == Double.class) || (target == float.class && value == Float.class) || (target == short.class && value == Short.class) || (target == byte.class && value == Byte.class) || (target == char.class && value == Character.class);
        }
        return false;
    }

    private static Object coerce(Class<?> target, String s) {
        if (s == null) return null;
        if (target == String.class) return s;
        if (target == LocalDateTime.class) return LocalDateTime.parse(s);
        if (target.isEnum()) return tryParseEnum(target, s);

        // Números básicos (se você vier a usá-los)
        if (target == Integer.class || target == int.class) return Integer.valueOf(s);
        if (target == Long.class || target == long.class) return Long.valueOf(s);
        if (target == Double.class || target == double.class) return Double.valueOf(s);
        if (target == Float.class || target == float.class) return Float.valueOf(s);
        if (target == Boolean.class || target == boolean.class) return Boolean.valueOf(s);

        // fallback: não convertido
        return s;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object tryParseEnum(Class<?> enumType, String name) {
        if (name == null) return null;
        return Enum.valueOf((Class<? extends Enum>) enumType, name);
    }

    private static Class<?> findEnumTypeInCtor(Constructor<?> ctor, int index) {
        Class<?>[] types = ctor.getParameterTypes();
        if (index >= 0 && index < types.length && types[index].isEnum()) {
            return types[index];
        }
        // fallback: primeira posição enum
        for (Class<?> t : types) if (t.isEnum()) return t;
        throw new IllegalStateException("Nenhum parâmetro enum encontrado no construtor " + ctor);
    }

    private static Constructor<?> findCompatibleConstructor(Class<?> clazz, Class<?>[] desired) {
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            Class<?>[] p = c.getParameterTypes();
            if (p.length != desired.length) continue;
            boolean ok = true;
            for (int i = 0; i < p.length; i++) {
                if (!(p[i].isEnum() && desired[i] == Enum.class) && !p[i].isAssignableFrom(desired[i])) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                c.setAccessible(true);
                return c;
            }
        }
        throw new IllegalStateException("Nenhum construtor compatível encontrado em " + clazz.getName());
    }

    // ====== INSTANTIA O MEMENTO ======

    public void escreverChaves(List<ChavePix> chaves) {
        List<String> linhas = chaves.stream().map(ChavePix::toMemento).map(this::formatarLinha).collect(Collectors.toList());
        fileOperations.escreverLinhas(linhas);
    }


    // ====== HELPERS DE TIPO ======

    public List<ChavePix> lerTodasAsChaves() {
        return fileOperations.lerLinhas().stream().map(this::parseLinha).filter(Objects::nonNull).map(ChavePix::fromMemento).collect(Collectors.toList());
    }

    private String formatarLinha(ChavePix.Memento chave) {
        return chave.toString();
    }

    private ChavePix.Memento parseLinha(String linha) {
        if (linha == null || linha.isBlank()) return null;

        // 1) Bate o outer Memento[ ... ]
        Matcher mOuter = OUTER_PATTERN.matcher(linha);
        if (!mOuter.find()) return null;

        String inside = mOuter.group(1);

        // 2) Quebra em pares chave=valor (com suporte ao bloco DadosBancarios[...])
        Map<String, String> rootKVs = new LinkedHashMap<>();
        Matcher mKV = KV_PATTERN.matcher(inside);
        while (mKV.find()) {
            rootKVs.put(mKV.group(1), mKV.group(2).trim());
        }

        // 3) Parse DadosBancarios se existir
        Object dadosBancariosObj = null;
        if (rootKVs.containsKey("dadosBancarios")) {
            String dbRaw = rootKVs.get("dadosBancarios");
            dadosBancariosObj = parseDadosBancarios(dbRaw);
        }

        // 4) Monta valores parseados por nome de campo
        Map<String, Object> parsed = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : rootKVs.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            if (k.equals("dadosBancarios")) {
                parsed.put(k, dadosBancariosObj);
            } else {
                parsed.put(k, normalizeScalar(v));
            }
        }

        // 5) Instancia o Memento via reflexão tentando casar um construtor
        try {
            return instantiateMemento(parsed);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Falha ao instanciar ChavePix.Memento: " + ex.getMessage(), ex);
        }
    }

    private Object parseDadosBancarios(String raw) {
        Matcher m = DB_INNER_PATTERN.matcher(raw);
        if (!m.find()) {
            throw new IllegalArgumentException("Formato inválido de DadosBancarios: " + raw);
        }
        String inside = m.group(1);

        Map<String, String> kv = new LinkedHashMap<>();
        Matcher mKV = DB_KV_PATTERN.matcher(inside);
        while (mKV.find()) {
            kv.put(mKV.group(1), mKV.group(2).trim());
        }

        // Constrói o record DadosBancarios refletindo a ordem dos componentes
        try {
            Class<?> dbClass = DadosBancarios.class;
            if (!dbClass.isRecord()) {
                // fallback: tenta um ctor público com 4 parâmetros
                Constructor<?> ctor = findCompatibleConstructor(dbClass, new Class<?>[]{String.class, String.class, String.class, Enum.class});
                Object tipoConta = tryParseEnum(findEnumTypeInCtor(ctor, 3), kv.get("tipoDeContaBancaria"));
                return ctor.newInstance(kv.get("agencia"), kv.get("conta"), kv.get("codigoBanco"), tipoConta);
            }

            RecordComponent[] components = dbClass.getRecordComponents();
            Object[] args = new Object[components.length];
            for (int i = 0; i < components.length; i++) {
                RecordComponent rc = components[i];
                String name = rc.getName();
                Class<?> type = rc.getType();
                String value = kv.get(name);
                args[i] = coerce(type, value);
            }

            // Chama o canonical constructor do record
            Class<?>[] paramTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
            Constructor<?> canonical = dbClass.getDeclaredConstructor(paramTypes);
            canonical.setAccessible(true);
            return canonical.newInstance(args);

        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Erro criando DadosBancarios por reflexão", e);
        }
    }

    private ChavePix.Memento instantiateMemento(Map<String, Object> parsed) throws ReflectiveOperationException {

        Class<ChavePix.Memento> mClass = ChavePix.Memento.class;

        if (!mClass.isRecord()) {
            throw new IllegalStateException("ChavePix.Memento não é um record.");
        }

        // Ordem e tipos canônicos do record
        RecordComponent[] components = mClass.getRecordComponents();
        Class<?>[] paramTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new);

        Object[] args = new Object[components.length];

        for (int i = 0; i < components.length; i++) {
            RecordComponent rc = components[i];
            String name = rc.getName();
            Class<?> type = rc.getType();

            Object val = parsed.get(name); // pode ser null (ex.: dataExpiracao)

            // Coerções necessárias quando veio como String
            if (val != null) {
                if (type.isEnum() && val instanceof String s) {
                    val = tryParseEnum(type, s);
                } else if (type == java.time.LocalDateTime.class && val instanceof String s) {
                    val = java.time.LocalDateTime.parse(s);
                } else if (!type.isInstance(val)) {
                    // fallback: tenta coercer básicos (string -> número/boolean), se você quiser
                    val = coerce(type, Objects.toString(val, null));
                }
            } else {
                // null em tipos primitivos não é permitido
                if (type.isPrimitive()) {
                    throw new IllegalArgumentException("Campo '" + name + "' é primitivo e não pode ser null.");
                }
            }

            args[i] = val;
        }

        Constructor<ChavePix.Memento> canonical = mClass.getDeclaredConstructor(paramTypes);
        canonical.setAccessible(true);
        return canonical.newInstance(args);
    }

    private Object normalizeScalar(String raw) {
        String v = raw.trim();
        if ("null".equalsIgnoreCase(v)) return null;

        // Tente LocalDateTime
        if (v.length() >= 19 && v.contains("T")) {
            try {
                return LocalDateTime.parse(v);
            } catch (Exception ignored) {
            }
        }
        // Deixe como String por padrão; enums serão convertidos depois pelo tipo-alvo
        return v;
    }
}
