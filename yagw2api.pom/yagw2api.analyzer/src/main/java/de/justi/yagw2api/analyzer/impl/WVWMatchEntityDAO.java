package de.justi.yagw2api.analyzer.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.time.LocalDateTime;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.IWVWMatchEntityDAO;
import de.justi.yagw2api.analyzer.IWVWScoresEmbeddable;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWScores;

public final class WVWMatchEntityDAO implements IWVWMatchEntityDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWMatchEntityDAO.class);

	@Override
	public synchronized Optional<IWVWMatchEntity> newMatchEntityOf(final IWVWMatch match) {
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
			this.synchronizeEntityWithModel(newEntity, LocalDateTime.now(), match);
			YAGW2APIAnalyzerPersistence.getDefaultEM().persist(newEntity);
			YAGW2APIAnalyzerPersistence.getDefaultEM().flush();
			if (newTransaction) {
				tx.commit();
			}
		} catch (Exception e) {
			LOGGER.error("Exception cought while creating new {}  out of {}", WVWMatchEntity.class, match, e);
			if (tx.isActive()) {
				tx.rollback();
			}
			newEntity = null;
		}
		return Optional.<IWVWMatchEntity> fromNullable(newEntity);
	}

	@Override
	public synchronized boolean save(final IWVWMatchEntity entity) {
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
			LOGGER.error("Exception cought while saving {}", entity, e);
			if (tx.isActive() && !tx.getRollbackOnly()) {
				tx.rollback();
			}
			success = false;
		}
		return success;
	}

	@Override
	public synchronized Optional<IWVWMatchEntity> findWVWMatchEntity(final LocalDateTime start, final LocalDateTime end, final String originMatchId) {
		checkNotNull(start);
		checkNotNull(end);
		checkNotNull(originMatchId);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Query q = YAGW2APIAnalyzerPersistence.getDefaultEM().createQuery(
				"SELECT DISTINCT m FROM wvw_match m WHERE m.matchId=:matchId AND m.startTimestamp=:startTimestamp AND m.endTimestamp=:endTimestamp");
		q.setParameter("matchId", originMatchId);
		q.setParameter("startTimestamp", start);
		q.setParameter("endTimestamp", end);
		try {
			return Optional.<IWVWMatchEntity> of((IWVWMatchEntity) q.getSingleResult());
		} catch (NoResultException e) {
			return Optional.absent();
		} catch (NonUniqueResultException e) {
			throw new IllegalStateException("More that result returned for query that should return only one single " + IWVWMatchEntity.class.getSimpleName()
					+ " identified by start=" + start + ", end=" + end + " and originMatchId=" + originMatchId, e);
		}
	}

	@Override
	public synchronized IWVWMatchEntity findOrCreateWVWMatchEntityOf(final IWVWMatch match) {
		checkNotNull(match);
		checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
		final Optional<IWVWMatchEntity> alreadyThere = this.findWVWMatchEntity(match.getStartTimestamp(), match.getEndTimestamp(), match.getId());
		if (alreadyThere.isPresent()) {
			LOGGER.trace("Found match: {}", match);
			return alreadyThere.get();
		} else {
			final Optional<IWVWMatchEntity> newCreated = this.newMatchEntityOf(match);
			checkState(newCreated.isPresent());
			LOGGER.trace("Created new match: {}", match);
			return newCreated.get();
		}
	}

	private boolean doesModelScoreDiffFromEntityScore(final IWVWScoresEmbeddable scoreEntity, final IWVWScores scoreModel) {
		return (scoreEntity.getBlueScore() != scoreModel.getBlueScore()) || (scoreEntity.getGreenScore() != scoreModel.getGreenScore())
				|| (scoreEntity.getRedScore() != scoreModel.getRedScore());
	}

	@Override
	public synchronized void synchronizeEntityWithModel(final IWVWMatchEntity entity, final LocalDateTime timestamp, final IWVWMatch model) {
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
			entity.getBlueMap().addScores(
					timestamp,
					new WVWScoresEmbeddable(model.getBlueMap().getScores().getRedScore(), model.getBlueMap().getScores().getGreenScore(), model.getBlueMap().getScores()
							.getBlueScore()));
		}
		checkState(entity.getGreenMap() != null);
		entity.getGreenMap().setType(model.getGreenMap().getType());
		final Optional<IWVWScoresEmbeddable> latestGreenMapScores = entity.getGreenMap().getLatestScores();
		if (!latestGreenMapScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestGreenMapScores.get(), model.getGreenMap().getScores()))) {
			entity.getGreenMap().addScores(
					timestamp,
					new WVWScoresEmbeddable(model.getGreenMap().getScores().getRedScore(), model.getGreenMap().getScores().getGreenScore(), model.getGreenMap().getScores()
							.getBlueScore()));
		}
		checkState(entity.getRedMap() != null);
		entity.getRedMap().setType(model.getRedMap().getType());
		final Optional<IWVWScoresEmbeddable> latestRedMapScores = entity.getRedMap().getLatestScores();
		if (!latestRedMapScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestRedMapScores.get(), model.getRedMap().getScores()))) {
			entity.getRedMap().addScores(
					timestamp,
					new WVWScoresEmbeddable(model.getRedMap().getScores().getRedScore(), model.getRedMap().getScores().getGreenScore(), model.getRedMap().getScores()
							.getBlueScore()));
		}
		checkState(entity.getCenterMap() != null);
		entity.getCenterMap().setType(model.getCenterMap().getType());
		final Optional<IWVWScoresEmbeddable> latestCenterMapScores = entity.getCenterMap().getLatestScores();
		if (!latestCenterMapScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestCenterMapScores.get(), model.getCenterMap().getScores()))) {
			entity.getCenterMap().addScores(
					timestamp,
					new WVWScoresEmbeddable(model.getCenterMap().getScores().getRedScore(), model.getCenterMap().getScores().getGreenScore(), model.getCenterMap().getScores()
							.getBlueScore()));
		}

		// synchronize timestamps
		entity.setStartTimestamp(model.getStartTimestamp());
		entity.setEndTimestamp(model.getEndTimestamp());

		final Optional<IWVWScoresEmbeddable> latestScores = entity.getLatestScores();
		LOGGER.trace("Latest score={}", latestScores.orNull());
		if (!latestScores.isPresent() || (this.doesModelScoreDiffFromEntityScore(latestScores.get(), model.getScores()))) {
			entity.addScores(timestamp, new WVWScoresEmbeddable(model.getScores().getRedScore(), model.getScores().getGreenScore(), model.getScores().getBlueScore()));
		}

		LOGGER.trace("Synchronized {} with {} @{}", entity, model, timestamp);
	}
}
