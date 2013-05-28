package de.justi.yagw2api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.justi.yagw2api.utils.InjectionHelper;
import de.justi.yagw2api.wrapper.IWVWWrapper;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InjectionHelper.INSTANCE.getInjector().getInstance(IWVWWrapper.class).start();

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("de.justi.yagw2api");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().commit();

		em.close();
	}

}
