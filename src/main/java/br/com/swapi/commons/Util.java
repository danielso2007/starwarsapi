package br.com.swapi.commons;

public final class Util {

	private Util() {
	}

	// Padrão Singleton
	private static class SingletonHolder {
		public static final Util instance = new Util();
	}

	public static Util getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Converte o nome de uma classe em nome de variável padrão, "descapitalizando"
	 * a primeira letra
	 */
	public String varName(Class<?> clazz) {
		String className = clazz.getSimpleName();
		char[] c = className.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}

}
