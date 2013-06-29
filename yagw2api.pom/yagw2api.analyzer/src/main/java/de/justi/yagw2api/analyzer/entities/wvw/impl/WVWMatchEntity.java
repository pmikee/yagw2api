package de.justi.yagw2api.analyzer.entities.wvw.impl;

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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.impl.WorldEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMapEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWScoresEmbeddable;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWMapType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.WVWMapType;

@Entity(name = "wvw_match")
public final class WVWMatchEntity extends AbstractEntity implements IWVWMatchEntity {

	@Column(name = "match_id", nullable = false)
	private String matchId = null;

	@JoinColumn(name = "redworld")
	@ManyToOne(targetEntity = WorldEntity.class, cascade = { CascadeType.ALL })
	private IWorldEntity redWorld = null;

	@JoinColumn(name = "greenworld")
	@ManyToOne(targetEntity = WorldEntity.class, cascade = { CascadeType.ALL })
	private IWorldEntity greenWorld = null;

	@JoinColumn(name = "blueworld")
	@ManyToOne(targetEntity = WorldEntity.class, cascade = { CascadeType.ALL })
	private IWorldEntity blueWorld = null;

	@OneToMany(targetEntity = WVWMapEntity.class, mappedBy = "match", cascade = { CascadeType.ALL })
	@MapKeyColumn(name = "mapType")
	@MapKeyClass(WVWMapType.class)
	private Map<IWVWMapType, IWVWMapEntity> maps = new CopyOnWriteHashMap<IWVWMapType, IWVWMapEntity>();

	@Column(name = "startOfMatch")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTimestamp = null;

	@Column(name = "endOfMatch")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTimestamp = null;

	@ElementCollection(targetClass = WVWScoresEmbeddable.class)
	@MapKeyColumn(name = "timestamp")
	@MapKeyTemporal(TemporalType.TIMESTAMP)
	@Column(name = "scores")
	@CollectionTable()
	private final Map<Date, IWVWScoresEmbeddable> scoresMappedByTimestamp = new CopyOnWriteHashMap<Date, IWVWScoresEmbeddable>();

	@Override
	public String getOriginMatchId() {
		return this.matchId;
	}

	@Override
	public Date getStartTimestamp() {
		return this.startTimestamp;
	}

	@Override
	public Date getEndTimestamp() {
		return this.endTimestamp;
	}

	@Override
	public IWorldEntity getRedWorld() {
		return this.redWorld;
	}

	@Override
	public IWorldEntity getGreenWorld() {
		return this.greenWorld;
	}

	@Override
	public IWorldEntity getBlueWorld() {
		return this.blueWorld;
	}

	@Override
	public Map<Date, IWVWScoresEmbeddable> getScores() {
		checkState(this.scoresMappedByTimestamp != null);
		return Collections.unmodifiableMap(this.scoresMappedByTimestamp);
	}

	@Override
	public void addScores(Date timestamp, IWVWScoresEmbeddable scores) {
		this.scoresMappedByTimestamp.put(checkNotNull(timestamp), checkNotNull(scores));
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

	@Override
	public void setOriginMatchId(String originMatchId) {
		this.matchId = checkNotNull(originMatchId);
	}

	@Override
	public void setRedMap(IWVWMapEntity map) {
		checkNotNull(map);
		checkState(this.getRedMap() == null);
		this.maps.put(WVWMapType.RED, map);
	}

	@Override
	public void setGreenMap(IWVWMapEntity map) {
		checkNotNull(map);
		checkState(this.getGreenMap() == null);
		this.maps.put(WVWMapType.GREEN, map);
	}

	@Override
	public void setBlueMap(IWVWMapEntity map) {
		checkNotNull(map);
		checkState(this.getBlueMap() == null);
		this.maps.put(WVWMapType.BLUE, map);
	}

	@Override
	public void setCenterMap(IWVWMapEntity map) {
		checkNotNull(map);
		checkState(this.getCenterMap() == null);
		this.maps.put(WVWMapType.CENTER, map);
	}

	@Override
	public void setRedWorld(IWorldEntity world) {
		checkNotNull(world);
		checkState(this.getRedWorld() == null);
		this.redWorld = world;
	}

	@Override
	public void setGreenWorld(IWorldEntity world) {
		checkNotNull(world);
		checkState(this.getGreenWorld() == null);
		this.greenWorld = world;
	}

	@Override
	public void setBlueWorld(IWorldEntity world) {
		checkNotNull(world);
		checkState(this.getBlueWorld() == null);
		this.blueWorld = world;
	}

	@Override
	public void setStartTimestamp(Date date) {
		this.startTimestamp = checkNotNull(date);
	}

	@Override
	public void setEndTimestamp(Date date) {
		this.endTimestamp = checkNotNull(date);
	}
}
