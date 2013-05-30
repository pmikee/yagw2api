package de.justi.yagw2api.analyzer.entities.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWWorldEnityDAO;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWWorldEntity;

class WVWWorldEntityDAO implements IWVWWorldEnityDAO {
	private static final Logger LOGGER = Logger.getLogger(WVWWorldEntityDAO.class);

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IWVWWorldEntity> retrieveAllWorldEntities() {
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT w FROM world w");
		return q.getResultList();
	}

	@Override
	public Optional<IWVWWorldEntity> findWorldEntityById(int id) {
		checkArgument(id >= 0);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		return Optional.<IWVWWorldEntity> fromNullable(YAGW2APIAnalyzerPersistence.getDefaultEM().find(WVWWorldEntity.class, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IWVWWorldEntity> searchWorldEntityByNamePart(String part) {
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT w FROM world w WHERE w.nameDE LIKE :part");
		q.setParameter("part", "%" + part + "%");
		return q.getResultList();
	}

	@Override
	public Optional<IWVWWorldEntity> findWorldEntityByName(String name) {
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT DISTINCT w FROM world w WHERE w.nameDE=:name");
		q.setParameter("name", name);
		try {
			return Optional.<IWVWWorldEntity> of((WVWWorldEntity) q.getSingleResult());
		} catch (NoResultException e) {
			return Optional.absent();
		}
	}

	@Override
	public Optional<IWVWWorldEntity> newWorldEntity(String name) {
		checkNotNull(name);
		checkArgument(name.trim().length() > 0);
		final EntityTransaction tx = YAGW2APIAnalyzerPersistence.getDefaultEM().getTransaction();
		WVWWorldEntity newEntity = null;
		boolean isNewTransaction = tx.isActive() == false;
		try {
			if (isNewTransaction) {
				tx.begin();
			}
			newEntity = new WVWWorldEntity(name);
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(newEntity);
			if (isNewTransaction) {
				tx.commit();
			}
		} catch (PersistenceException | IllegalStateException e) {
			LOGGER.error("Exception cought while creating new " + WVWWorldEntity.class.getName(), e);
			newEntity = null;
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return Optional.<IWVWWorldEntity> fromNullable(newEntity);
	}

}
