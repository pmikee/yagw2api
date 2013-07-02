package de.justi.yagw2api.analyzer.entities.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
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

import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntityDAO;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWScoresEmbeddable;
import de.justi.yagw2api.wrapper.model.IWorld;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.IWVWScores;

public final class WVWMatchEntityDAO implements IWVWMatchEntityDAO {
	private static final Logger LOGGER = Logger.getLogger(WVWMatchEntityDAO.class);

	@Override
	public synchronized Optional<IWVWMatchEntity> newMatchEntityOf(IWVWMatch match) {
		checkNotNull(match);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final EntityTransaction tx = YAGW2APIAnalyzerPersistence.getDefaultEM().getTransaction();
		WVWMatchEntity newEntity = null;
		final boolean newTransaction = !tx.isActive();
		try {
			if (newTransaction) {
				tx.begin();
			}
			newEntity = new WVWMatchEntity();
			this.synchronizeEntityWithModel(newEntity, new Date(), match);
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(newEntity);
			YAGW2APIAnalyzerPersistence.getDefaultEM().flush();
			if (newTransaction) {
				tx.commit();
			}
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
			if (newTransaction) {
				tx.begin();
			}
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(entity);
			YAGW2APIAnalyzerPersistence.getDefaultEM().flush();
			if (newTransaction) {
				tx.commit();
			}
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
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Found already existing " + IWVWMatchEntity.class.getSimpleName() + " for " + IWVWMatch.class.getSimpleName() + " matchId=" + match.getId());
			}
			return alreadyThere.get();
		} else {
			final Optional<IWVWMatchEntity> newCreated = this.newMatchEntityOf(match);
			checkState(newCreated.isPresent());
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Created new not persisted " + IWVWMatchEntity.class.getSimpleName() + " for " + IWVWMatch.class.getSimpleName() + " matchId=" + match.getId());
			}
			return newCreated.get();
		}
	}

	private boolean doesModelScoreDiffFromEntityScore(IWVWScoresEmbeddable scoreEntity, IWVWScores scoreModel) {
		return (scoreEntity.getBlueScore() != scoreModel.getBlueScore()) || (scoreEntity.getGreenScore() != scoreModel.getGreenScore()) || (scoreEntity.getRedScore() != scoreModel.getRedScore());
	}

	@Override
	public synchronized void synchronizeEntityWithModel(IWVWMatchEntity entity, Date timestamp, IWVWMatch model) {
		checkNotNull(entity);
		checkNotNull(timestamp);
		checkNotNull(model);
		checkArgument((entity.getOriginMatchId() == null) || model.getId().equals(entity.getOriginMatchId()));
		// compatible ids
		entity.setOriginMatchId(model.getId());

		// setup world references
		if (entity.getBlueWorld() == null) {
			entity.setBlueWorld(YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getBlueWorld()));
			entity.getBlueWorld().addParticipatedAsBlueInMatch(entity);
		}
		if (entity.getGreenWorld() == null) {
			entity.setGreenWorld(YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getGreenWorld()));
			entity.getGreenWorld().addParticipatedAsGreenInMatch(entity);
		}
		if (entity.getRedWorld() == null) {
			entity.setRedWorld(YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getRedWorld()));
			entity.getRedWorld().addParticipatedAsRedInMatch(entity);
		}

		// setup map references
		if (entity.getBlueMap() == null) {
			entity.setBlueMap(new WVWMapEntity());
			entity.getBlueMap().setMatch(entity);
		}
		if (entity.getRedMap() == null) {
			entity.setRedMap(new WVWMapEntity());
			entity.getRedMap().setMatch(entity);
		}

		if (entity.getGreenMap() == null) {
			entity.setGreenMap(new WVWMapEntity());
			entity.getGreenMap().setMatch(entity);

		}
		if (entity.getCenterMap() == null) {
			entity.setCenterMap(new WVWMapEntity());
			entity.getCenterMap().setMatch(entity);
		}
		checkState(entity.getBlueMap() != null);
		entity.getBlueMap().setType(model.getBlueMap().getType());
		final Optional<IWVWScoresEmbeddable> latestBlueMapScores = entity.getBlueMap().getLatestScores();
		if (!latestBlueMapScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestBlueMapScores.get(), model.getBlueMap().getScores()))) {
			entity.getBlueMap().addScores(timestamp,
					new WVWScoresEmbeddable(model.getBlueMap().getScores().getRedScore(), model.getBlueMap().getScores().getGreenScore(), model.getBlueMap().getScores().getBlueScore()));
		}
		checkState(entity.getGreenMap() != null);
		entity.getGreenMap().setType(model.getGreenMap().getType());
		final Optional<IWVWScoresEmbeddable> latestGreenMapScores = entity.getGreenMap().getLatestScores();
		if (!latestGreenMapScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestGreenMapScores.get(), model.getGreenMap().getScores()))) {
			entity.getGreenMap().addScores(timestamp,
					new WVWScoresEmbeddable(model.getGreenMap().getScores().getRedScore(), model.getGreenMap().getScores().getGreenScore(), model.getGreenMap().getScores().getBlueScore()));
		}
		checkState(entity.getRedMap() != null);
		entity.getRedMap().setType(model.getRedMap().getType());
		final Optional<IWVWScoresEmbeddable> latestRedMapScores = entity.getRedMap().getLatestScores();
		if (!latestRedMapScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestRedMapScores.get(), model.getRedMap().getScores()))) {
			entity.getRedMap().addScores(timestamp,
					new WVWScoresEmbeddable(model.getRedMap().getScores().getRedScore(), model.getRedMap().getScores().getGreenScore(), model.getRedMap().getScores().getBlueScore()));
		}
		checkState(entity.getCenterMap() != null);
		entity.getCenterMap().setType(model.getCenterMap().getType());
		final Optional<IWVWScoresEmbeddable> latestCenterMapScores = entity.getCenterMap().getLatestScores();
		if (!latestCenterMapScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestCenterMapScores.get(), model.getCenterMap().getScores()))) {
			entity.getCenterMap().addScores(timestamp,
					new WVWScoresEmbeddable(model.getCenterMap().getScores().getRedScore(), model.getCenterMap().getScores().getGreenScore(), model.getCenterMap().getScores().getBlueScore()));
		}

		// synchronize timestamps
		entity.setStartTimestamp(model.getStartTimestamp().getTime());
		entity.setEndTimestamp(model.getEndTimestamp().getTime());

		final Optional<IWVWScoresEmbeddable> latestScores = entity.getLatestScores();
		if (LOGGER.isDebugEnabled() && latestScores.isPresent()) {
			LOGGER.debug("Latest score=" + latestScores.get());
		}
		if (!latestScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestScores.get(), model.getScores()))) {
			entity.addScores(timestamp, new WVWScoresEmbeddable(model.getScores().getRedScore(), model.getScores().getGreenScore(), model.getScores().getBlueScore()));
		}

		LOGGER.trace("Synchronized " + entity.getId() + " with " + model.getId() + " @ " + timestamp);
	}
}
