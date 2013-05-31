package de.justi.yagw2api.analyzer.entities;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.net.MalformedURLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.eclipse.persistence.exceptions.PersistenceUnitLoadingException;

import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;

public enum YAGW2APIAnalyzerPersistence {
	DEFAULT("de.justi.yagw2api"), TEST("de.justi.yagw2api.test");

	private static final Logger LOGGER = Logger.getLogger(YAGW2APIAnalyzerPersistence.class);
	
	public static EntityManager getDefaultEM() {
		return YAGW2APIAnalyzer.getInjector().getInstance(YAGW2APIAnalyzerPersistence.class).getEM();
	}

	public static EntityManagerFactory getDefaultEMF() {
		return YAGW2APIAnalyzer.getInjector().getInstance(YAGW2APIAnalyzerPersistence.class).getEMF();
	}

	private final String persistenceUnitName;
	private EntityManagerFactory emf = null;
	private EntityManager em = null;

	private YAGW2APIAnalyzerPersistence(String persistenceUnitName) {
		checkNotNull(persistenceUnitName);
		this.persistenceUnitName = persistenceUnitName;
	}
	
	public final EntityManagerFactory getEMF() {
		checkState(this.persistenceUnitName != null);
		if (this.emf == null) {
			synchronized (this) {
				if (this.emf == null) {
					LOGGER.info("Going to initialize "+EntityManagerFactory.class.getSimpleName()+" for persistenceUnitName="+this.persistenceUnitName);
					try {
						this.emf = Persistence.createEntityManagerFactory(this.persistenceUnitName);
					}catch (PersistenceUnitLoadingException e) {
						
					}
				}
			}
		}
		checkState(this.emf != null);
		if(!this.emf.isOpen()) {
			synchronized (this) {
				if(!this.emf.isOpen()) {		
					this.emf = null;
					this.emf = this.getEMF();
				}
			}
		}
		checkState(this.emf != null);
		return this.emf;
	}

	public final EntityManager getEM() {
		if(this.em == null) {
			synchronized (this) {
				if (this.em == null) {
					this.em = this.getEMF().createEntityManager();
				}
			}
		}
		checkState(this.em != null);
		if(!this.em.isOpen()) {
			synchronized (this) {
				if(!this.em.isOpen()) {		
					this.em = null;
					this.em = this.getEM();
				}
			}
		}
		checkState(this.em != null);
		return em;
	}
}
