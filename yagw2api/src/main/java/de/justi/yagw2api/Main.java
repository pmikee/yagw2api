package de.justi.yagw2api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import de.justi.yagw2api.utils.InjectionHelper;
import de.justi.yagw2api.utils.PersistenceHelper;
import de.justi.yagw2api.wrapper.IWVWWrapper;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InjectionHelper.getInjector().getInstance(IWVWWrapper.class).start();

			EntityManager em = PersistenceHelper.getSharedEntityManager();
			em.getTransaction().begin();
			em.getTransaction().commit();

		} catch (Exception e) {
			LOGGER.fatal("Uncought exception while running "+Main.class.getName()+"#main(String[])", e);
		} finally {
			PersistenceHelper.getEntityManagerFactory().close();
		}
	}

}
