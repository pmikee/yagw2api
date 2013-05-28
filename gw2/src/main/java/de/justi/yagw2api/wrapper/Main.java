package de.justi.yagw2api.wrapper;

import de.justi.yagw2api.utils.InjectionHelper;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InjectionHelper.INSTANCE.getInjector().getInstance(IWVW.class).start();
	}

}
