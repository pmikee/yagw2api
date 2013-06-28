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
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyTemporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.ObjectTypeConverters;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMapEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWScoresEmbeddable;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWMapType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.WVWMapType;

@ObjectTypeConverters({ @ObjectTypeConverter(name = "MapTypeConverter", objectType = WVWMapType.class, dataType = String.class, conversionValues = {
		@ConversionValue(objectValue = "CENTER", dataValue = "CENTER"), @ConversionValue(objectValue = "RED", dataValue = "RED"), @ConversionValue(objectValue = "GREEN", dataValue = "GREEN"),
		@ConversionValue(objectValue = "BLUE", dataValue = "BLUE") }) })
@Entity(name = "map")
public final class WVWMapEntity extends AbstractEntity implements IWVWMapEntity {
	private static final Logger LOGGER = Logger.getLogger(WVWMapEntity.class);

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

	public WVWMapEntity() {
	}

	public WVWMapEntity(IWVWMatchEntity parentMatch) {
		checkNotNull(parentMatch);
		this.match = parentMatch;
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
	public synchronized void synchronizeWithModel(Date timestamp, IWVWMap model, boolean setupMatchReference) {
		checkNotNull(model);

		this.type = model.getType();

		if (setupMatchReference) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Going to setup match reference of " + this);
			}
			if ((this.match == null) && model.getMatch().isPresent()) {
				this.match = YAGW2APIAnalyzer.getWVWMatchEntityDAO().findOrCreateWVWMatchEntityOf(model.getMatch().get());
			}
		}

		final Optional<IWVWScoresEmbeddable> latestScores = this.getLatestScores();

		if (!latestScores.isPresent() || (latestScores.get().getBlueScore() != model.getScores().getBlueScore()) || (latestScores.get().getGreenScore() != model.getScores().getGreenScore())
				|| (latestScores.get().getRedScore() != model.getScores().getRedScore())) {
			this.addScores(timestamp, model.getScores().getRedScore(), model.getScores().getGreenScore(), model.getScores().getBlueScore());
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Synchronized " + this.getClass().getSimpleName() + " " + this.getId() + " with " + model.getType() + " @ " + timestamp);
		}
	}

	private void addScores(Date timestamp, int redScore, int greenScore, int blueScore) {
		checkNotNull(timestamp);
		checkArgument(redScore >= 0);
		checkArgument(greenScore >= 0);
		checkArgument(blueScore >= 0);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Going to add " + timestamp + " redScore=" + redScore + ", greenScore=" + greenScore + ", blueScore=" + blueScore + " to " + this);
			System.out.println(this.scoresMappedByTimestamp);
		}

		checkState(this.scoresMappedByTimestamp != null);
		checkState(!this.scoresMappedByTimestamp.containsKey(timestamp), this + " already contains a score for the given timestamp: " + timestamp);
		// TODO make use of factory to create WVWScoresEmbeddable
		this.scoresMappedByTimestamp.put(timestamp, new WVWScoresEmbeddable(redScore, greenScore, blueScore));
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
