package de.justi.yagw2api.analyzer.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
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

import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.MapKeyConvert;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;

import de.justi.yagw2api.analyzer.IWVWMapEntity;
import de.justi.yagw2api.analyzer.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.IWVWScoresEmbeddable;
import de.justi.yagw2api.analyzer.IWorldEntity;
import de.justi.yagw2api.wrapper.IWVWMapType;
import de.justi.yagw2api.wrapper.impl.WVWMapType;

@Entity(name = "wvw_match")
@Converter(name="localdatetime_converter", converterClass=LocalDateTimeConverter.class)
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
	private Map<IWVWMapType, IWVWMapEntity> maps = Maps.newHashMap();

	@Column(name = "startOfMatch")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter=LocalDateTimeConverter.class)
	private LocalDateTime startTimestamp = null;

	@Column(name = "endOfMatch")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter=LocalDateTimeConverter.class)
	private LocalDateTime endTimestamp = null;

	@ElementCollection(targetClass = WVWScoresEmbeddable.class)
	@MapKeyColumn(name = "timestamp")
	@MapKeyTemporal(TemporalType.TIMESTAMP)
	@MapKeyConvert("localdatetime_converter")
	@Column(name = "scores")
	@CollectionTable
	private final Map<LocalDateTime, IWVWScoresEmbeddable> scoresMappedByTimestamp = Maps.newHashMap();

	@Override
	public String getOriginMatchId() {
		return this.matchId;
	}

	@Override
	public LocalDateTime getStartTimestamp() {
		return this.startTimestamp;
	}

	@Override
	public LocalDateTime getEndTimestamp() {
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
	public Map<LocalDateTime, IWVWScoresEmbeddable> getScores() {
		checkState(this.scoresMappedByTimestamp != null);
		return Collections.unmodifiableMap(this.scoresMappedByTimestamp);
	}

	@Override
	public void addScores(LocalDateTime timestamp, IWVWScoresEmbeddable scores) {
		this.scoresMappedByTimestamp.put(checkNotNull(timestamp), checkNotNull(scores));
	}

	@Override
	public Optional<IWVWScoresEmbeddable> getLatestScores() {
		checkState(this.scoresMappedByTimestamp != null);
		if (this.scoresMappedByTimestamp.isEmpty()) {
			return Optional.absent();
		} else {
			final SortedSet<LocalDateTime> keys = ImmutableSortedSet.copyOf(this.scoresMappedByTimestamp.keySet());
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
	public ToStringHelper toStringHelper() {
		return super.toStringHelper().add("matchId", this.matchId).add("latestScores", this.getLatestScores().orNull()).add("scoreHistorySize", this.scoresMappedByTimestamp.size())
				.add("start", this.startTimestamp).add("end", this.endTimestamp);
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
	public void setStartTimestamp(LocalDateTime date) {
		this.startTimestamp = checkNotNull(date);
	}

	@Override
	public void setEndTimestamp(LocalDateTime date) {
		this.endTimestamp = checkNotNull(date);
	}
}
