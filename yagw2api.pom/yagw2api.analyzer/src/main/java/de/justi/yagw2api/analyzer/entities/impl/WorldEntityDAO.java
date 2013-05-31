package de.justi.yagw2api.analyzer.entities.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.IWorldEnityDAO;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.core.wrapper.model.IWorld;

class WorldEntityDAO implements IWorldEnityDAO {
	private static final Logger LOGGER = Logger.getLogger(WorldEntityDAO.class);

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IWorldEntity> retrieveAllWorldEntities() {
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT w FROM world w");
		final Optional<List<IWorldEntity>> qResultBuffer = Optional.<List<IWorldEntity>>fromNullable(q.getResultList());
		final Collection<IWorldEntity> result;		
		if(qResultBuffer.isPresent()) {
			result = Collections.unmodifiableCollection(new ArrayList<IWorldEntity>(qResultBuffer.get()));
		}else {
			 result = Collections.EMPTY_LIST;
		}
		return result;
	}

	@Override
	public Optional<IWorldEntity> findWorldEntityById(int id) {
		checkArgument(id >= 0);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		return Optional.<IWorldEntity> fromNullable(YAGW2APIAnalyzerPersistence.getDefaultEM().find(WorldEntity.class, id));
	}
	
	@Override
	public Optional<IWorldEntity> findWorldEntityByOriginId(int originId) {
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

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IWorldEntity> searchWorldEntityByNamePart(String part) {
		checkNotNull(part);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT w FROM world w WHERE w.nameDE LIKE :part");
		q.setParameter("part", "%" + part + "%");
		final Optional<List<IWorldEntity>> qResultBuffer = Optional.<List<IWorldEntity>>fromNullable(q.getResultList());		
		final Collection<IWorldEntity> result;		
		if(qResultBuffer.isPresent()) {
			result = Collections.unmodifiableCollection(new ArrayList<IWorldEntity>(qResultBuffer.get()));
		}else {
			 result = Collections.EMPTY_LIST;
		}
		return result;
	}

	@Override
	public Optional<IWorldEntity> findWorldEntityByName(String name) {
		checkNotNull(name);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery("SELECT DISTINCT w FROM world w WHERE w.nameDE=:name");
		q.setParameter("name", name);
		try {
			return Optional.<IWorldEntity> of((WorldEntity) q.getSingleResult());
		} catch (NoResultException e) {
			return Optional.absent();
		}
	}

	@Override
	public Optional<IWorldEntity> newWorldEntityOf(IWorld world) {
		checkNotNull(world);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final EntityTransaction tx = YAGW2APIAnalyzerPersistence.getDefaultEM().getTransaction();
		WorldEntity newEntity = null;
		boolean isNewTransaction = tx.isActive() == false;
		try {
			if (isNewTransaction) {
				tx.begin();
			}
			newEntity = new WorldEntity();
			newEntity.setNameDE(world.getName().get());
			newEntity.setOriginId(world.getId());
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(newEntity);
			if (isNewTransaction) {
				tx.commit();
			}
		} catch (PersistenceException | IllegalStateException e) {
			LOGGER.error("Exception cought while creating new " + WorldEntity.class.getName(), e);
			newEntity = null;
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return Optional.<IWorldEntity> fromNullable(newEntity);
	}

	@Override
	public Optional<IWorldEntity> findWorldEntityOf(IWorld world) {
		checkNotNull(world);
		checkArgument(world.getId() > 0);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		return this.findWorldEntityByOriginId(world.getId());
	}

	@Override
	public IWorldEntity findOrCreateWorldEntityOf(IWorld world) {
		checkNotNull(world);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Optional<IWorldEntity> alreadyThere = this.findWorldEntityOf(world);
		if(alreadyThere.isPresent()) {
			return alreadyThere.get();
		}else {
			final Optional<IWorldEntity> newCreated = this.newWorldEntityOf(world);
			checkState(newCreated.isPresent());
			return newCreated.get();
		}		
	}

	

}
