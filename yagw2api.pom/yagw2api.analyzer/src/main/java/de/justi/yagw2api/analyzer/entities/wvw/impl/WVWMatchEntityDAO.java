package de.justi.yagw2api.analyzer.entities.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntityDAO;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

public final class WVWMatchEntityDAO implements IWVWMatchEntityDAO {
	private static final Logger LOGGER = Logger.getLogger(WVWMatchEntityDAO.class);

	@Override
	public synchronized Optional<IWVWMatchEntity> newMatchEntityOf(IWVWMatch match, boolean setupWorldReferences) {
		checkNotNull(match);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final EntityTransaction tx = YAGW2APIAnalyzerPersistence.getDefaultEM().getTransaction();
		WVWMatchEntity newEntity = null;
		final boolean newTransaction = !tx.isActive();
		try {
			if (newTransaction)
				tx.begin();
			newEntity = new WVWMatchEntity();
			newEntity.synchronizeWithModel(new Date(), match, setupWorldReferences);
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(newEntity);
			YAGW2APIAnalyzerPersistence.getDefaultEM().flush();
			if (newTransaction)
				tx.commit();
		} catch (Exception e) {
			LOGGER.error(
					"Exception cought while creating new " + WVWMatchEntity.class.getName() + " out of " + match.getClass().getSimpleName() + "[matchId=" + match.getId() + ",redWorld="
							+ match.getRedWorld() + ", greenWorld=" + match.getGreenWorld() + ", blueWorld=" + match.getBlueWorld() + "]", e);
			newEntity = null;
			if (tx.isActive()) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Going to rollback " + EntityTransaction.class.getSimpleName() + " for new " + IWorldEntity.class.getSimpleName() + " creation out of " + IWorld.class.getSimpleName()
							+ " for " + newEntity);
				}
				tx.rollback();
			}
		}
		return Optional.<IWVWMatchEntity> fromNullable(newEntity);
	}

	@Override
	public synchronized boolean save(IWVWMatchEntity entity) {
		boolean success;
		final EntityTransaction tx = YAGW2APIAnalyzerPersistence.getDefaultEM().getTransaction();
		final boolean newTransaction = !tx.isActive();
		try {
			if (newTransaction)
				tx.begin();
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(entity);
			YAGW2APIAnalyzerPersistence.getDefaultEM().flush();
			if (newTransaction)
				tx.commit();
			success = true;
		} catch (Exception e) {
			LOGGER.error("Exception cought while saving " + entity, e);
			if (tx.isActive() && !tx.getRollbackOnly()) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Going to rollback " + EntityTransaction.class.getSimpleName() + " for saving " + IWVWMatchEntity.class.getSimpleName() + ": " + entity);
				}
				tx.rollback();
			}
			success = false;
		}
		return success;
	}

	@Override
	public synchronized Optional<IWVWMatchEntity> findWVWMatchEntity(Date start, Date end, String originMatchId) {
		checkNotNull(start);
		checkNotNull(end);
		checkNotNull(originMatchId);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery(
				"SELECT DISTINCT m FROM wvw_match m WHERE m.matchId=:matchId AND m.startTimestamp=:startTimestamp AND m.endTimestamp=:endTimestamp");
		q.setParameter("matchId", originMatchId);
		q.setParameter("startTimestamp", new Timestamp(start.getTime()));
		q.setParameter("endTimestamp", new Timestamp(end.getTime()));
		try {
			return Optional.<IWVWMatchEntity> of((IWVWMatchEntity) q.getSingleResult());
		} catch (NoResultException e) {
			return Optional.absent();
		} catch (NonUniqueResultException e) {
			LOGGER.fatal("More that result returned for query that should return only one single " + IWVWMatchEntity.class.getSimpleName() + " identified by start=" + start + ", end=" + end
					+ " and originMatchId=" + originMatchId, e);
			throw new IllegalStateException("More that result returned for query that should return only one single " + IWVWMatchEntity.class.getSimpleName() + " identified by start=" + start
					+ ", end=" + end + " and originMatchId=" + originMatchId, e);
		}
	}

	@Override
	public synchronized IWVWMatchEntity findOrCreateWVWMatchEntityOf(IWVWMatch match) {
		checkNotNull(match);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Optional<IWVWMatchEntity> alreadyThere = this.findWVWMatchEntity(match.getStartTimestamp().getTime(), match.getEndTimestamp().getTime(), match.getId());
		if (alreadyThere.isPresent()) {
			return alreadyThere.get();
		} else {
			final Optional<IWVWMatchEntity> newCreated = this.newMatchEntityOf(match, true);
			checkState(newCreated.isPresent());
			return newCreated.get();
		}
	}
}
