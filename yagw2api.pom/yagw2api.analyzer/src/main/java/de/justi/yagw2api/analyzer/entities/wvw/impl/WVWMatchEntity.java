package de.justi.yagw2api.analyzer.entities.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyTemporal;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.impl.WorldEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWScoresEmbeddable;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

@Entity(name = "wvw_match")
public class WVWMatchEntity extends AbstractEntity implements IWVWMatchEntity {
	private static final Logger LOGGER = Logger.getLogger(WVWMatchEntity.class);

	@Column(name = "match_id", nullable = false)
	private String matchId;

	@JoinColumn(name = "redworld")
	@ManyToOne(targetEntity = WorldEntity.class, cascade = { CascadeType.ALL })
	private IWorldEntity redWorld;

	@JoinColumn(name = "greenworld")
	@ManyToOne(targetEntity = WorldEntity.class, cascade = { CascadeType.ALL })
	private IWorldEntity greenWorld;

	@JoinColumn(name = "blueworld")
	@ManyToOne(targetEntity = WorldEntity.class, cascade = { CascadeType.ALL })
	private IWorldEntity blueWorld;

	@Column(name = "startOfMatch")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTimestamp;

	@Column(name = "endOfMatch")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTimestamp;

	@ElementCollection(targetClass = WVWScoresEmbeddable.class)
	@MapKeyColumn(name = "timestamp")
	@MapKeyTemporal(TemporalType.TIMESTAMP)
	@Column(name = "scores")
	@CollectionTable()
	private Map<Date, IWVWScoresEmbeddable> scoresMappedByTimestamp = new CopyOnWriteHashMap<Date, IWVWScoresEmbeddable>();

	@Override
	public String getOriginMatchId() {
		checkState(this.matchId != null);
		return this.matchId;
	}

	@Override
	public Date getStartTimestamp() {
		checkState(this.startTimestamp != null);
		return this.startTimestamp;
	}

	@Override
	public Date getEndTimestamp() {
		checkState(this.endTimestamp != null);
		return this.endTimestamp;
	}

	@Override
	public IWorldEntity getRedWorld() {
		checkState(this.redWorld != null);
		return this.redWorld;
	}

	@Override
	public IWorldEntity getGreenWorld() {
		checkState(this.greenWorld != null);
		return this.greenWorld;
	}

	@Override
	public IWorldEntity getBlueWorld() {
		checkState(this.blueWorld != null);
		return this.blueWorld;
	}

	private boolean checkIfWorldOfMatchModelAreCompatible(IWVWMatch match) {
		return (this.blueWorld == null || (this.blueWorld.getOriginWorldId().isPresent() && this.blueWorld.getOriginWorldId().get().equals(match.getBlueWorld().getId())))
				&& (this.greenWorld == null || (this.greenWorld.getOriginWorldId().isPresent() && this.greenWorld.getOriginWorldId().get().equals(match.getGreenWorld().getId())))
				&& (this.redWorld == null || (this.redWorld.getOriginWorldId().isPresent() && this.redWorld.getOriginWorldId().get().equals(match.getRedWorld().getId())));
	}

	@Override
	public void synchronizeWithModel(Date timestamp, IWVWMatch model, boolean setupWorldReferences) {
		checkNotNull(model);
		checkArgument(this.matchId == null || model.getId().equals(this.matchId));
		checkArgument(setupWorldReferences == false || this.checkIfWorldOfMatchModelAreCompatible(model));
		// compatible ids
		this.matchId = model.getId();

		// synchronize blue world
		if (setupWorldReferences && this.blueWorld == null) {
			final IWorldEntity world = YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getBlueWorld());
			this.blueWorld = world;
			this.blueWorld.addParticipatedAsBlueInMatch(this);
		}

		// synchronize green world
		if (setupWorldReferences && this.greenWorld == null) {
			final IWorldEntity world = YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getGreenWorld());
			this.greenWorld = world;
			this.greenWorld.addParticipatedAsGreenInMatch(this);
		}

		// synchronize red world
		if (setupWorldReferences && this.redWorld == null) {
			final IWorldEntity world = YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getRedWorld());
			this.redWorld = world;
			this.redWorld.addParticipatedAsRedInMatch(this);
		}

		// synchronize timestamps
		this.startTimestamp = model.getStartTimestamp().getTime();
		this.endTimestamp = model.getEndTimestamp().getTime();

		final Optional<IWVWScoresEmbeddable> latestScores = this.getLatestScores();
		if(LOGGER.isDebugEnabled() && latestScores.isPresent()) {
			LOGGER.debug("Latest score="+latestScores.get());
		}
		if (!latestScores.isPresent() || 
			 latestScores.get().getBlueScore() != model.getScores().getBlueScore() || 
			 latestScores.get().getGreenScore() != model.getScores().getGreenScore() || 
			 latestScores.get().getRedScore() != model.getScores().getRedScore()) 
		{
			// TODO make use of factory to create WVWScoresEmbeddable
			this.addScores(timestamp, model.getScores().getRedScore(), model.getScores().getGreenScore(), model.getScores().getBlueScore());
		}

		LOGGER.trace("Synchronized " + this.getId() + " with " + model.getId() + " @ " + timestamp);
	}

	@Override
	public Map<Date, IWVWScoresEmbeddable> getScores() {
		checkState(this.scoresMappedByTimestamp != null);
		return Collections.unmodifiableMap(this.scoresMappedByTimestamp);
	}

	private void addScores(Date timestamp, int redScore, int greenScore, int blueScore) {
		checkNotNull(timestamp);
		checkArgument(redScore >= 0);
		checkArgument(greenScore >= 0);
		checkArgument(blueScore >= 0);
		checkState(this.scoresMappedByTimestamp != null);
		checkState(!this.scoresMappedByTimestamp.containsKey(timestamp));
		this.scoresMappedByTimestamp.put(timestamp, new WVWScoresEmbeddable(redScore, greenScore, blueScore));
	}

	@Override
	public Optional<IWVWScoresEmbeddable> getLatestScores() {
		checkState(this.scoresMappedByTimestamp != null);
		if (this.scoresMappedByTimestamp.isEmpty()) {
			return Optional.absent();
		} else {
			final SortedSet<Date> keys = ImmutableSortedSet.copyOf(this.scoresMappedByTimestamp.keySet());
			return Optional.of(this.scoresMappedByTimestamp.get(keys.last()));
		}
	}
}
