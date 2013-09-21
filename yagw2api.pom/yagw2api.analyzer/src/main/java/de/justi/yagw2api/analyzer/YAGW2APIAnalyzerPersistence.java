package de.justi.yagw2api.analyzer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.eclipse.persistence.config.SystemProperties;
import org.eclipse.persistence.exceptions.PersistenceUnitLoadingException;
import org.eclipse.persistence.jpa.ArchiveFactory;

import de.justi.yagw2api.analyzer.utils.ArchiveFactoryImpl;

public enum YAGW2APIAnalyzerPersistence {
	DEFAULT("yagw2api"), TEST("yagw2api_test");

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
					if (LOGGER.isInfoEnabled()) {
						System.setProperty(SystemProperties.ARCHIVE_FACTORY, ArchiveFactoryImpl.class.getName());
						LOGGER.info("Going to initialize " + EntityManagerFactory.class.getSimpleName() + " for persistenceUnitName=" + this.persistenceUnitName);
						final String factoryClassName = System.getProperty(SystemProperties.ARCHIVE_FACTORY, null);
						LOGGER.info(ArchiveFactory.class.getName() + " to use from vmarg: " + factoryClassName);
					}
					try {
						Map<String, String> properties = new HashMap<String, String>();
						this.emf = Persistence.createEntityManagerFactory(this.persistenceUnitName, properties);
					} catch (PersistenceUnitLoadingException e) {
						LOGGER.fatal("Unexpected Exception thrown during creation of " + EntityManagerFactory.class.getSimpleName() + ". Resource name was " + e.getResourceName(), e);
						this.emf = null;
					}
				}
			}
		}
		checkState(this.emf != null);
		if (!this.emf.isOpen()) {
			synchronized (this) {
				if (!this.emf.isOpen()) {
					this.emf = null;
					this.emf = this.getEMF();
				}
			}
		}
		checkState(this.emf != null);
		return this.emf;
	}

	public final EntityManager getEM() {
		if (this.em == null) {
			synchronized (this) {
				if (this.em == null) {
					this.em = this.getEMF().createEntityManager();
				}
			}
		}
		checkState(this.em != null);
		if (!this.em.isOpen()) {
			synchronized (this) {
				if (!this.em.isOpen()) {
					this.em = null;
					this.em = this.getEM();
				}
			}
		}
		checkState(this.em != null);
		return em;
	}
}
