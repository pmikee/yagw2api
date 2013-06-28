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
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyTemporal;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.impl.WorldEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMapEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWScoresEmbeddable;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWMapType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.WVWMapType;

@Entity(name = "wvw_match")
public final class WVWMatchEntity extends AbstractEntity implements IWVWMatchEntity {
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

	@OneToMany(targetEntity = WVWMapEntity.class, mappedBy = "match", cascade = { CascadeType.ALL })
	@MapKeyColumn(name = "mapType")
	@MapKeyClass(WVWMapType.class)
	private Map<IWVWMapType, WVWMapEntity> maps;

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
	private final Map<Date, IWVWScoresEmbeddable> scoresMappedByTimestamp = new CopyOnWriteHashMap<Date, IWVWScoresEmbeddable>();

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
		return ((this.blueWorld == null) || (this.blueWorld.getOriginWorldId().isPresent() && this.blueWorld.getOriginWorldId().get().equals(match.getBlueWorld().getId())))
				&& ((this.greenWorld == null) || (this.greenWorld.getOriginWorldId().isPresent() && this.greenWorld.getOriginWorldId().get().equals(match.getGreenWorld().getId())))
				&& ((this.redWorld == null) || (this.redWorld.getOriginWorldId().isPresent() && this.redWorld.getOriginWorldId().get().equals(match.getRedWorld().getId())));
	}

	@Override
	public synchronized void synchronizeWithModel(Date timestamp, IWVWMatch model, boolean setupWorldReferences, boolean setuptMapReferences) {
		checkNotNull(model);
		checkArgument((this.matchId == null) || model.getId().equals(this.matchId));
		checkArgument((setupWorldReferences == false) || this.checkIfWorldOfMatchModelAreCompatible(model));
		// compatible ids
		this.matchId = model.getId();

		// setup world references
		if (setupWorldReferences) {
			if (this.blueWorld == null) {
				this.blueWorld = YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getBlueWorld());
				this.blueWorld.addParticipatedAsBlueInMatch(this);
			}
			if (this.greenWorld == null) {
				this.greenWorld = YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getGreenWorld());
				this.greenWorld.addParticipatedAsGreenInMatch(this);
			}
			if (this.redWorld == null) {
				this.redWorld = YAGW2APIAnalyzer.getWorldEntityDAO().findOrCreateWorldEntityOf(model.getRedWorld());
				this.redWorld.addParticipatedAsRedInMatch(this);
			}
		}

		// setup map references
		if (setuptMapReferences) {
			if (!this.maps.containsKey(WVWMapType.BLUE)) {
				this.maps.put(WVWMapType.BLUE, new WVWMapEntity(this));
			}
			if (!this.maps.containsKey(WVWMapType.GREEN)) {
				this.maps.put(WVWMapType.GREEN, new WVWMapEntity(this));
			}
			if (!this.maps.containsKey(WVWMapType.RED)) {
				this.maps.put(WVWMapType.RED, new WVWMapEntity(this));
			}
			if (!this.maps.containsKey(WVWMapType.CENTER)) {
				this.maps.put(WVWMapType.CENTER, new WVWMapEntity(this));
			}
		}
		if (this.maps.containsKey(WVWMapType.BLUE)) {
			this.maps.get(WVWMapType.BLUE).synchronizeWithModel(timestamp, model.getBlueMap(), false);
		}
		if (this.maps.containsKey(WVWMapType.GREEN)) {
			this.maps.get(WVWMapType.GREEN).synchronizeWithModel(timestamp, model.getGreenMap(), false);
		}
		if (this.maps.containsKey(WVWMapType.RED)) {
			this.maps.get(WVWMapType.RED).synchronizeWithModel(timestamp, model.getRedMap(), false);
		}
		if (this.maps.containsKey(WVWMapType.CENTER)) {
			this.maps.get(WVWMapType.CENTER).synchronizeWithModel(timestamp, model.getCenterMap(), false);
		}

		// synchronize timestamps
		this.startTimestamp = model.getStartTimestamp().getTime();
		this.endTimestamp = model.getEndTimestamp().getTime();

		final Optional<IWVWScoresEmbeddable> latestScores = this.getLatestScores();
		if (LOGGER.isDebugEnabled() && latestScores.isPresent()) {
			LOGGER.debug("Latest score=" + latestScores.get());
		}
		if (!latestScores.isPresent() || (latestScores.get().getBlueScore() != model.getScores().getBlueScore()) || (latestScores.get().getGreenScore() != model.getScores().getGreenScore())
				|| (latestScores.get().getRedScore() != model.getScores().getRedScore())) {
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
		// TODO make use of factory to create WVWScoresEmbeddable
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

	@Override
	public IWVWMapEntity getRedMap() {
		return this.maps.get(WVWMapType.RED);
	}

	@Override
	public IWVWMapEntity getGreenMap() {
		return this.maps.get(WVWMapType.GREEN);
	}

	@Override
	public IWVWMapEntity getBlueMap() {
		return this.maps.get(WVWMapType.BLUE);
	}

	@Override
	public IWVWMapEntity getCenterMap() {
		return this.maps.get(WVWMapType.CENTER);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("matchId", this.matchId).add("latestScores", this.getLatestScores().orNull()).add("scoreHistorySize", this.scoresMappedByTimestamp.size())
				.add("start", this.startTimestamp).add("end", this.endTimestamp).toString();
	}
}
