package de.justi.yagw2api.analyzer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
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

public enum YAGW2APIAnalyzerPersistence {
	DEFAULT("yagw2api"), TEST("yagw2api_test");

	public static EntityManager getDefaultEM() {
		return YAGW2APIAnalyzer.getInjector().getInstance(YAGW2APIAnalyzerPersistence.class).getEM();
	}

	public static EntityManagerFactory getDefaultEMF() {
		return YAGW2APIAnalyzer.getInjector().getInstance(YAGW2APIAnalyzerPersistence.class).getEMF();
	}

	private final String persistenceUnitName;
	private EntityManagerFactory emf = null;
	private EntityManager sharedEntityManager = null;

	private YAGW2APIAnalyzerPersistence(String persistenceUnitName) {
		checkNotNull(persistenceUnitName);
		this.persistenceUnitName = persistenceUnitName;
	}

	// FIXME the auto recreation of the emf is a major design flaw
	public final EntityManagerFactory getEMF() {
		checkState(this.persistenceUnitName != null);
		if (this.emf == null) {
			synchronized (this) {
				if (this.emf == null) {
					Map<String, String> properties = new HashMap<String, String>();
					this.emf = Persistence.createEntityManagerFactory(this.persistenceUnitName, properties);
				}
			}
		}
		checkState(this.emf != null);
		if(!this.emf.isOpen()){
			synchronized (this.emf) {
				if(!this.emf.isOpen()){
					this.emf = null;
					return this.getEMF();
				}
			}
		}
		return this.emf;
	}
	// FIXME the auto recreation of the shared em is a major design flaw
	public final EntityManager getEM() {
		if (this.sharedEntityManager == null) {
			synchronized (this) {
				if (this.sharedEntityManager == null) {
					this.sharedEntityManager = this.getEMF().createEntityManager();
				}
			}
		}
		checkState(this.sharedEntityManager != null);
		if(!this.sharedEntityManager.isOpen()){
			synchronized (this.sharedEntityManager) {
				if(!this.sharedEntityManager.isOpen()){
					this.sharedEntityManager = null;
					return this.getEM();
				}
			}
		}
		

		return this.sharedEntityManager;
	}
}
