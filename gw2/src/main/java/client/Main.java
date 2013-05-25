package client;

import utils.InjectionHelper;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InjectionHelper.INSTANCE.getInjector().getInstance(IClientApplication.class).start();
	}

}
