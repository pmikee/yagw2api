package de.justi.yagw2api;

import java.util.UUID;

import org.apache.log4j.Logger;

import de.justi.yagw2api.analyzer.wvw.entities.IWVWWorldEnityDAO;
import de.justi.yagw2api.utils.InjectionHelper;
import de.justi.yagw2api.utils.PersistenceHelper;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// InjectionHelper.getInjector().getInstance(IWVWWrapper.class).start();

			
			final IWVWWorldEnityDAO dao = InjectionHelper.getInjector().getInstance(IWVWWorldEnityDAO.class);
			System.out.println(dao.newWorldEntity(UUID.randomUUID().toString()).get());
			System.out.println(dao.retrieveAllWorldEntities());

		} catch (Exception e) {
			LOGGER.fatal("Uncought exception while running " + Main.class.getName() + "#main(String[])", e);
		} finally {
			PersistenceHelper.getEntityManagerFactory().close();
		}
	}

}
