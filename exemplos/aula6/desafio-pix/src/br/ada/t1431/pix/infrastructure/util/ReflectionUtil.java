package br.ada.t1431.pix.infrastructure.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;

/**
 * Utilitário para operações de reflexão em manipulação de tipos e construtores.
 * <p>
 * Fornece métodos estáticos para verificação de compatibilidade de tipos,
 * parsing de enums, identificação de tipos enum em construtores e busca de construtores compatíveis.
 * Usado principalmente para reconstrução de entidades via reflexão em persistência de dados.
 */
public class ReflectionUtil {
    /**
     * Verifica se o tipo de destino é compatível com o tipo de valor informado.
     * Suporta tipos primitivos e suas classes wrapper.
     * @param target tipo de destino
     * @param value tipo do valor
     * @return true se compatível, false caso contrário
     */
    public static boolean isAssignable(Class<?> target, Class<?> value) {
        if (target.isAssignableFrom(value)) return true;
        if (target.isPrimitive()) {
            return (target == boolean.class && value == Boolean.class) || (target == int.class && value == Integer.class) || (target == long.class && value == Long.class) || (target == double.class && value == Double.class) || (target == float.class && value == Float.class) || (target == short.class && value == Short.class) || (target == byte.class && value == Byte.class) || (target == char.class && value == Character.class);
        }
        return false;
    }

    /**
     * Realiza o parsing de uma string para um valor enum do tipo informado.
     * @param enumType classe do enum
     * @param name nome do valor do enum
     * @return valor do enum correspondente ou null se não houver
     */
    public static Object tryParseEnum(Class<?> enumType, String name) {
        if (name == null) return null;
        return Enum.valueOf((Class<? extends Enum>) enumType, name);
    }

    /**
     * Busca o tipo enum em um construtor, pelo índice ou pela primeira ocorrência.
     * @param ctor construtor
     * @param index índice do parâmetro
     * @return classe do tipo enum encontrada
     * @throws IllegalStateException se não encontrar parâmetro enum
     */
    public static Class<?> findEnumTypeInCtor(Constructor<?> ctor, int index) {
        Class<?>[] types = ctor.getParameterTypes();
        if (index >= 0 && index < types.length && types[index].isEnum()) {
            return types[index];
        }
        for (Class<?> t : types) if (t.isEnum()) return t;
        throw new IllegalStateException("Nenhum parâmetro enum encontrado no construtor " + ctor);
    }

    /**
     * Busca um construtor compatível com os tipos desejados.
     * @param clazz classe alvo
     * @param desired array de tipos desejados
     * @return construtor compatível
     * @throws IllegalStateException se não encontrar construtor compatível
     */
    public static Constructor<?> findCompatibleConstructor(Class<?> clazz, Class<?>[] desired) {
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
}
