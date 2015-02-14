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
import java.util.Map;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyTemporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.MapKeyConvert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.ObjectTypeConverters;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;

import de.justi.yagw2api.analyzer.IWVWMapEntity;
import de.justi.yagw2api.analyzer.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.IWVWScoresEmbeddable;
import de.justi.yagw2api.wrapper.IWVWMapType;
import de.justi.yagw2api.wrapper.impl.WVWMapType;

@ObjectTypeConverters({ @ObjectTypeConverter(name = "MapTypeConverter", objectType = WVWMapType.class, dataType = String.class, conversionValues = {
		@ConversionValue(objectValue = "CENTER", dataValue = "CENTER"), @ConversionValue(objectValue = "RED", dataValue = "RED"), @ConversionValue(objectValue = "GREEN", dataValue = "GREEN"),
		@ConversionValue(objectValue = "BLUE", dataValue = "BLUE") }) })
@Entity(name = "map")
@Converter(name="localdatetime_converter", converterClass=LocalDateTimeConverter.class)
public final class WVWMapEntity extends AbstractEntity implements IWVWMapEntity {
	@ElementCollection(targetClass = WVWScoresEmbeddable.class)
	@MapKeyColumn(name = "timestamp")
	@MapKeyTemporal(TemporalType.TIMESTAMP)
	@MapKeyConvert("localdatetime_converter")
	@Column(name = "scores")
	@CollectionTable()
	private final Map<LocalDateTime, IWVWScoresEmbeddable> scoresMappedByTimestamp = Maps.newHashMap();

	@ManyToOne(targetEntity = WVWMatchEntity.class, cascade = { CascadeType.ALL }, optional = false)
	private IWVWMatchEntity match = null;

	@Column(name = "map_type", unique = false, nullable = false)
	@Convert("MapTypeConverter")
	private IWVWMapType type = null;

	@Override
	public IWVWMapType getType() {
		return this.type;
	}

	@Override
	public void setType(IWVWMapType type) {
		this.type = checkNotNull(type);
	}

	@Override
	public void setMatch(IWVWMatchEntity match) {
		this.match = checkNotNull(match);
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
	public void addScores(LocalDateTime timestamp, IWVWScoresEmbeddable scores) {
		checkNotNull(timestamp);
		checkNotNull(scores);
		checkState(this.scoresMappedByTimestamp != null);
		checkState(!this.scoresMappedByTimestamp.containsKey(timestamp), this + " already contains a score for the given timestamp: " + timestamp);
		this.scoresMappedByTimestamp.put(timestamp, checkNotNull(scores));
	}

	@Override
	public IWVWMatchEntity getMatch() {
		return this.match;
	}

	@Override
	public Map<LocalDateTime, IWVWScoresEmbeddable> getScores() {
		checkState(this.scoresMappedByTimestamp != null);
		return Collections.unmodifiableMap(this.scoresMappedByTimestamp);
	}

	@Override
	public ToStringHelper toStringHelper() {
		return super.toStringHelper().add("matchId", this.match.getOriginMatchId()).add("type", this.type).add("latestScores", this.getLatestScores().orNull())
				.add("scoreHistorySize", this.scoresMappedByTimestamp.size());
	}
}
