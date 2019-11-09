package br.com.swapi.commons;

public final class Util {

    /**
     * Converte o nome de uma classe em nome de variável padrão, "descapitalizando"
     * a primeira letra
     */
    public static String varName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        char c[] = className.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }
    
}
