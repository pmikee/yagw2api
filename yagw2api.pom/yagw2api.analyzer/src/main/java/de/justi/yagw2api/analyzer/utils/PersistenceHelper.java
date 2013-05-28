package de.justi.yagw2api.analyzer.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum PersistenceHelper{
	INSTANCE;
	
	public static EntityManager getSharedEntityManager() {
		return INSTANCE.em;
	}
	public static EntityManagerFactory getEntityManagerFactory() {
		return INSTANCE.emf;
	}
	
	private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("de.justi.yagw2api");
	private final EntityManager em = emf.createEntityManager();
}
