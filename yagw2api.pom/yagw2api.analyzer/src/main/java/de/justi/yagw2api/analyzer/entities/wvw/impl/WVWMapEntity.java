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
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyTemporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.ObjectTypeConverters;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMapEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWScoresEmbeddable;
import de.justi.yagw2api.wrapper.IWVWMapType;
import de.justi.yagw2api.wrapper.impl.WVWMapType;

@ObjectTypeConverters({ @ObjectTypeConverter(name = "MapTypeConverter", objectType = WVWMapType.class, dataType = String.class, conversionValues = {
		@ConversionValue(objectValue = "CENTER", dataValue = "CENTER"), @ConversionValue(objectValue = "RED", dataValue = "RED"), @ConversionValue(objectValue = "GREEN", dataValue = "GREEN"),
		@ConversionValue(objectValue = "BLUE", dataValue = "BLUE") }) })
@Entity(name = "map")
public final class WVWMapEntity extends AbstractEntity implements IWVWMapEntity {
	@ElementCollection(targetClass = WVWScoresEmbeddable.class)
	@MapKeyColumn(name = "timestamp")
	@MapKeyTemporal(TemporalType.TIMESTAMP)
	@Column(name = "scores")
	@CollectionTable()
	private final Map<Date, IWVWScoresEmbeddable> scoresMappedByTimestamp = new CopyOnWriteHashMap<Date, IWVWScoresEmbeddable>();

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
			final SortedSet<Date> keys = ImmutableSortedSet.copyOf(this.scoresMappedByTimestamp.keySet());
			return Optional.of(this.scoresMappedByTimestamp.get(keys.last()));
		}
	}

	@Override
	public void addScores(Date timestamp, IWVWScoresEmbeddable scores) {
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
	public Map<Date, IWVWScoresEmbeddable> getScores() {
		checkState(this.scoresMappedByTimestamp != null);
		return Collections.unmodifiableMap(this.scoresMappedByTimestamp);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("matchId", this.match.getOriginMatchId()).add("type", this.type).add("latestScores", this.getLatestScores().orNull())
				.add("scoreHistorySize", this.scoresMappedByTimestamp.size()).toString();
	}
}
