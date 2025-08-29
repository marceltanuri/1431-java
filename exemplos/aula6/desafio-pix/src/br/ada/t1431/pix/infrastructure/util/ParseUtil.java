package br.ada.t1431.pix.infrastructure.util;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitário para parsing e conversão de valores em operações de persistência e leitura de dados.
 * <p>
 * Fornece métodos estáticos para conversão de tipos, normalização de valores,
 * extração de conteúdo por regex e extração de pares chave-valor de strings.
 * Usado principalmente para manipulação de dados em arquivos de chaves Pix.
 */
public class ParseUtil {
    /**
     * Converte uma string para o tipo alvo informado.
     * Suporta tipos básicos, enums e datas.
     * @param target classe do tipo alvo
     * @param s valor em string a ser convertido
     * @return valor convertido para o tipo alvo
     */
    public static Object coerce(Class<?> target, String s) {
        if (s == null) return null;
        if (target == String.class) return s;
        if (target == LocalDateTime.class) return LocalDateTime.parse(s);
        if (target.isEnum()) return ReflectionUtil.tryParseEnum(target, s);
        if (target == Integer.class || target == int.class) return Integer.valueOf(s);
        if (target == Long.class || target == long.class) return Long.valueOf(s);
        if (target == Double.class || target == double.class) return Double.valueOf(s);
        if (target == Float.class || target == float.class) return Float.valueOf(s);
        if (target == Boolean.class || target == boolean.class) return Boolean.valueOf(s);
        return s;
    }

    /**
     * Normaliza uma string para tipos básicos, convertendo datas e null.
     * @param raw valor em string a ser normalizado
     * @return valor convertido ou normalizado
     */
    public static Object normalizeScalar(String raw) {
        String v = raw.trim();
        if ("null".equalsIgnoreCase(v)) return null;
        if (v.length() >= 19 && v.contains("T")) {
            try {
                return LocalDateTime.parse(v);
            } catch (Exception ignored) {}
        }
        return v;
    }

    /**
     * Extrai o conteúdo de uma string usando um padrão regex.
     * @param linha string de entrada
     * @param pattern padrão regex para extração
     * @return conteúdo extraído ou null se não houver correspondência
     */
    public static String extrairConteudo(String linha, Pattern pattern) {
        Matcher m = pattern.matcher(linha);
        if (!m.find()) return null;
        return m.group(1);
    }

    /**
     * Extrai pares chave-valor de uma string usando um padrão regex.
     * @param inside string de entrada
     * @param pattern padrão regex para extração dos pares
     * @return mapa de pares chave-valor extraídos
     */
    public static Map<String, String> extrairParesChaveValor(String inside, Pattern pattern) {
        Map<String, String> result = new LinkedHashMap<>();
        Matcher mKV = pattern.matcher(inside);
        while (mKV.find()) {
            result.put(mKV.group(1), mKV.group(2).trim());
        }
        return result;
    }
}
