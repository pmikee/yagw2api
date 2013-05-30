package de.justi.yagw2api.analyzer.entities.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.wvw.IWVWWorldEnityDAO;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWWorldEntity;
import de.justi.yagw2api.analyzer.utils.PersistenceHelper;

class WVWWorldEntityDAO implements IWVWWorldEnityDAO {
	private static final Logger LOGGER = Logger.getLogger(WVWWorldEntityDAO.class);

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IWVWWorldEntity> retrieveAllWorldEntities() {
		checkState(PersistenceHelper.getSharedEntityManager().isOpen());
		final Query q = PersistenceHelper.getSharedEntityManager().createQuery("SELECT w FROM world w");
		return q.getResultList();
	}

	@Override
	public Optional<IWVWWorldEntity> findWorldEntityById(int id) {
		checkArgument(id >= 0);
		checkState(PersistenceHelper.getSharedEntityManager().isOpen());
		return Optional.<IWVWWorldEntity> fromNullable(PersistenceHelper.getSharedEntityManager().find(WVWWorldEntity.class, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IWVWWorldEntity> searchWorldEntityByNamePart(String part) {
		checkState(PersistenceHelper.getSharedEntityManager().isOpen());
		final Query q = PersistenceHelper.getSharedEntityManager().createQuery("SELECT w FROM world w WHERE w.name_de LIKE '%" + part + "%'");
		return q.getResultList();
	}

	@Override
	public Optional<IWVWWorldEntity> findWorldEntityByName(String name) {
		checkState(PersistenceHelper.getSharedEntityManager().isOpen());
		final Query q = PersistenceHelper.getSharedEntityManager().createQuery("SELECT DISTINCT w FROM world w WHERE w.name_de = " + name);
		return Optional.<IWVWWorldEntity> fromNullable((WVWWorldEntity) q.getSingleResult());
	}

	@Override
	public Optional<IWVWWorldEntity> newWorldEntity(String name) {
		checkNotNull(name);
		checkArgument(name.trim().length() > 0);
		final EntityTransaction tx = PersistenceHelper.getSharedEntityManager().getTransaction();
		WVWWorldEntity newEntity = null;
		boolean isNewTransaction = tx.isActive() == false;
		try {
			if (isNewTransaction) {
				tx.begin();
			}
			newEntity = new WVWWorldEntity(name);
			PersistenceHelper.getSharedEntityManager().persist(newEntity);
			if (isNewTransaction) {
				tx.commit();
			}
		} catch (PersistenceException | IllegalStateException e) {
			LOGGER.error("Exception cought while creating new " + WVWWorldEntity.class.getName(), e);
			newEntity = null;
			if(tx.isActive()) {
				tx.rollback();
			}
		}
		return Optional.<IWVWWorldEntity> fromNullable(newEntity);
	}

}
