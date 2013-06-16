package de.justi.yagw2api.analyzer.entities.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.IWorldEnityDAO;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.core.wrapper.model.IWorld;

class WorldEntityDAO implements IWorldEnityDAO {
	private static final Logger LOGGER = Logger.getLogger(WorldEntityDAO.class);

	@Override
	public synchronized Collection<IWorldEntity> retrieveAllWorldEntities() {
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT w FROM world w");
		@SuppressWarnings("unchecked")
		final Optional<List<IWorldEntity>> qResultBuffer = Optional.<List<IWorldEntity>> fromNullable(q.getResultList());
		final Collection<IWorldEntity> result;
		if (qResultBuffer.isPresent()) {
			result = Collections.unmodifiableCollection(new ArrayList<IWorldEntity>(qResultBuffer.get()));
		} else {
			result = Collections.emptyList();
		}
		return result;
	}

	@Override
	public synchronized Optional<IWorldEntity> findWorldEntityById(long id) {
		checkArgument(id >= 0);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		return Optional.<IWorldEntity> fromNullable(YAGW2APIAnalyzerPersistence.getDefaultEM().find(WorldEntity.class, id));
	}

	@Override
	public synchronized Optional<IWorldEntity> findWorldEntityByOriginId(int originId) {
		checkArgument(originId > 0);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT DISTINCT w FROM world w WHERE w.originWorldId=:originId");
		q.setParameter("originId", originId);
		try {
			return Optional.<IWorldEntity> of((WorldEntity) q.getSingleResult());
		} catch (NoResultException e) {
			return Optional.absent();
		}
	}

	@Override
	public synchronized Collection<IWorldEntity> searchWorldEntityByNamePart(String part) {
		checkNotNull(part);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT w FROM world w WHERE w.nameDE LIKE :part OR w.nameEN LIKE :part OR w.nameES LIKE :part OR w.nameFR LIKE :part");
		q.setParameter("part", "%" + part + "%");
		@SuppressWarnings("unchecked")
		final Optional<List<IWorldEntity>> qResultBuffer = Optional.<List<IWorldEntity>> fromNullable(q.getResultList());
		final Collection<IWorldEntity> result;
		if (qResultBuffer.isPresent()) {
			result = Collections.unmodifiableCollection(new ArrayList<IWorldEntity>(qResultBuffer.get()));
		} else {
			result = Collections.emptyList();
		}
		return result;
	}

	@Override
	public synchronized Optional<IWorldEntity> findWorldEntityByName(Locale locale, String name) {
		checkNotNull(locale);
		checkNotNull(name);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());

		final Query q;
		switch (locale.getLanguage().toLowerCase()) {
			case "de":
				q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT DISTINCT w FROM world w WHERE w.nameDE=:name");
				break;
			case "en":
				q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT DISTINCT w FROM world w WHERE w.nameEN=:name");
				break;
			case "es":
				q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT DISTINCT w FROM world w WHERE w.nameES=:name");
				break;
			case "fr":
				q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT DISTINCT w FROM world w WHERE w.nameFR=:name");
				break;
			default:
				LOGGER.error("Unsupported locale: " + locale);
				throw new IllegalArgumentException("Unsupported locale: " + locale);
		}
		q.setParameter("name", name);
		try {
			return Optional.<IWorldEntity> of((WorldEntity) q.getSingleResult());
		} catch (NoResultException e) {
			return Optional.absent();
		}
	}

	@Override
	public synchronized Collection<IWorldEntity> searchWorldEntityByName(String name) {
		checkNotNull(name);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT w FROM world w WHERE w.nameDE=:name OR w.nameEN=:name OR w.nameES=:name OR w.nameFR=:name");
		q.setParameter("name", "name");
		@SuppressWarnings("unchecked")
		final Optional<List<IWorldEntity>> qResultBuffer = Optional.<List<IWorldEntity>> fromNullable(q.getResultList());
		final Collection<IWorldEntity> result;
		if (qResultBuffer.isPresent()) {
			result = Collections.unmodifiableCollection(new ArrayList<IWorldEntity>(qResultBuffer.get()));
		} else {
			result = Collections.emptyList();
		}
		return result;
	}

	@Override
	public synchronized Optional<IWorldEntity> newWorldEntityOf(IWorld world) {
		checkNotNull(world);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final EntityTransaction tx = YAGW2APIAnalyzerPersistence.getDefaultEM().getTransaction();
		WorldEntity newEntity = null;
		final boolean newTransaction = !tx.isActive();
		try {
			if(newTransaction) tx.begin();
			newEntity = new WorldEntity();
			checkState(newEntity.synchronizeWithModel(world));
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(newEntity);
			YAGW2APIAnalyzerPersistence.getDefaultEM().flush();
			if(newTransaction) tx.commit();
		} catch (Exception e) {
			LOGGER.error("Exception cought while creating new " + WorldEntity.class.getName()+" out of "+world, e);
			newEntity = null;
			if (tx.isActive()) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Going to rollback " + EntityTransaction.class.getSimpleName() + " for new " + IWorldEntity.class.getSimpleName() + " creation out of " + IWorld.class.getSimpleName()
							+ " for " + newEntity);
				}
				tx.rollback();
			}
		}
		return Optional.<IWorldEntity> fromNullable(newEntity);
	}

	@Override
	public synchronized Optional<IWorldEntity> findWorldEntityOf(IWorld world) {
		checkNotNull(world);
		checkArgument(world.getId() > 0);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		return this.findWorldEntityByOriginId(world.getId());
	}

	@Override
	public synchronized IWorldEntity findOrCreateWorldEntityOf(IWorld world) {
		checkNotNull(world);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Optional<IWorldEntity> alreadyThere = this.findWorldEntityOf(world);
		if (alreadyThere.isPresent()) {
			return alreadyThere.get();
		} else {
			final Optional<IWorldEntity> newCreated = this.newWorldEntityOf(world);
			checkState(newCreated.isPresent());
			return newCreated.get();
		}
	}

	@Override
	public synchronized boolean save(IWorldEntity entity) {
		boolean success;
		final EntityTransaction tx = YAGW2APIAnalyzerPersistence.getDefaultEM().getTransaction();
		final boolean newTransaction = !tx.isActive();
		try {
			if(newTransaction) tx.begin();
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(entity);
			YAGW2APIAnalyzerPersistence.getDefaultEM().flush();
			if(newTransaction) tx.commit();
			success = true;
		} catch (Exception e) {
			LOGGER.error("Exception cought while saving " + entity, e);
			if (tx.isActive() && !tx.getRollbackOnly()) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Going to rollback " + EntityTransaction.class.getSimpleName() + " for saving " + IWorldEntity.class.getSimpleName() + ": " + entity);
				}
				tx.rollback();
			}
			success = false;
		}
		return success;
	}
}
