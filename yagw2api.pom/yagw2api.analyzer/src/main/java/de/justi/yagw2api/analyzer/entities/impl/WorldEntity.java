package de.justi.yagw2api.analyzer.entities.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;
import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.ObjectTypeConverters;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.analyzer.entities.wvw.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.entities.wvw.impl.WVWMatchEntity;
import de.justi.yagw2api.analyzer.utils.converter.LocaleConverter;
import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.types.IWorldLocationType;
import de.justi.yagw2api.core.wrapper.model.types.WorldLocationType;

@ObjectTypeConverters({ @ObjectTypeConverter(name = "WorldLocationTypeConverter", objectType = WorldLocationType.class, dataType = String.class, conversionValues = {
		@ConversionValue(objectValue = "NORTH_AMERICA", dataValue = "NA"), @ConversionValue(objectValue = "EUROPE", dataValue = "EU") }) })
@Converters({ @Converter(converterClass = LocaleConverter.class, name = "LocaleConverter") })
@Entity(name = "world")
public final class WorldEntity extends AbstractEntity implements IWorldEntity {
	private static final Logger LOGGER = Logger.getLogger(WorldEntity.class);

	@Column(name = "name_de", unique = true, nullable = true)
	private String nameDE;

	@Column(name = "name_es", unique = true, nullable = true)
	private String nameES;

	@Column(name = "name_fr", unique = true, nullable = true)
	private String nameFR;

	@Column(name = "name_en", unique = true, nullable = true)
	private String nameEN;

	@Column(name = "origin_id", unique = true, nullable = false)
	private Integer originWorldId;

	@Column(name = "world_locale", unique = false, nullable = true)
	@Convert("LocaleConverter")
	private Locale worldLocale;

	@Column(name = "world_location", unique = false, nullable = false)
	@Convert("WorldLocationTypeConverter")
	private IWorldLocationType location;

	@OneToMany(targetEntity = WVWMatchEntity.class, mappedBy = "redWorld", cascade = { CascadeType.ALL })
	private List<WVWMatchEntity> participatedInMatchesAsRedWorld;

	@OneToMany(targetEntity = WVWMatchEntity.class, mappedBy = "greenWorld", cascade = { CascadeType.ALL })
	private List<WVWMatchEntity> participatedInMatchesAsGreenWorld;

	@OneToMany(targetEntity = WVWMatchEntity.class, mappedBy = "blueWorld", cascade = { CascadeType.ALL })
	private List<WVWMatchEntity> participatedInMatchesAsBlueWorld;

	protected WorldEntity() {
		super();
		this.nameDE = null;
		this.originWorldId = null;
		this.worldLocale = null;
		this.location = null;
		this.participatedInMatchesAsRedWorld = new ArrayList<WVWMatchEntity>();
		this.participatedInMatchesAsGreenWorld = new ArrayList<WVWMatchEntity>();
		this.participatedInMatchesAsBlueWorld = new ArrayList<WVWMatchEntity>();
	}

	@Override
	public boolean addParticipatedAsRedInMatch(IWVWMatchEntity match) {
		checkNotNull(match);
		if (this.participatedInMatchesAsBlueWorld.contains(match) || this.participatedInMatchesAsGreenWorld.contains(match) || this.participatedInMatchesAsRedWorld.contains(match)) {
			return false;
		} else {
			return this.participatedInMatchesAsRedWorld.add((WVWMatchEntity) match);
		}
	}

	@Override
	public boolean addParticipatedAsBlueInMatch(IWVWMatchEntity match) {
		checkNotNull(match);
		if (this.participatedInMatchesAsBlueWorld.contains(match) || this.participatedInMatchesAsGreenWorld.contains(match) || this.participatedInMatchesAsRedWorld.contains(match)) {
			return false;
		} else {
			return this.participatedInMatchesAsBlueWorld.add((WVWMatchEntity) match);
		}
	}

	@Override
	public boolean addParticipatedAsGreenInMatch(IWVWMatchEntity match) {
		checkNotNull(match);
		if (this.participatedInMatchesAsBlueWorld.contains(match) || this.participatedInMatchesAsGreenWorld.contains(match) || this.participatedInMatchesAsRedWorld.contains(match)) {
			return false;
		} else {
			return this.participatedInMatchesAsGreenWorld.add((WVWMatchEntity) match);
		}
	}

	@Override
	public IWorldLocationType getLocation() {
		return this.location;
	}

	@Override
	public Optional<Locale> getWorldLocale() {
		return Optional.fromNullable(this.worldLocale);
	}

	@Override
	public boolean synchronizeWithModel(IWorld model) {
		checkNotNull(model);
		if (this.originWorldId == null || model.getId() == this.originWorldId) {
			// compatible ids
			this.originWorldId = model.getId();
			if (model.getName().isPresent()) {
				this.setName(YAGW2APICore.getCurrentLocale(), model.getName().get());
			}
			this.worldLocale = model.getWorldLocale().orNull();
			this.location = model.getWorldLocation();

			return true;
		} else {
			// incompatible ids
			return false;
		}
	}

	private void setName(Locale locale, String name) {
		checkNotNull(locale);
		checkNotNull(name);
		checkArgument(name.trim().length() > 0);
		switch (locale.getLanguage().toLowerCase()) {
			case "de":
				this.nameDE = name;
				break;
			case "en":
				this.nameEN = name;
				break;
			case "es":
				this.nameES = name;
				break;
			case "fr":
				this.nameFR = name;
				break;
			default:
				LOGGER.error("Unsupported locale: " + locale);
				throw new IllegalArgumentException("Unsupported locale: " + locale);
		}
	}

	@Override
	public Optional<String> getName(Locale locale) {
		switch (locale.getLanguage().toLowerCase()) {
			case "de":
				return this.getNameDE().or(this.getNameEN()).or(this.getNameES()).or(this.getNameFR());
			case "en":
				return this.getNameEN().or(this.getNameDE()).or(this.getNameES()).or(this.getNameFR());
			case "es":
				return this.getNameES().or(this.getNameEN()).or(this.getNameDE()).or(this.getNameFR());
			case "fr":
				return this.getNameFR().or(this.getNameEN()).or(this.getNameDE()).or(this.getNameES());
			default:
				LOGGER.error("Unsupported locale: " + locale);
				throw new IllegalArgumentException("Unsupported locale: " + locale);
		}
	}

	@Override
	public Optional<String> getName() {
		return this.getName(YAGW2APICore.getCurrentLocale());
	}

	@Override
	public Optional<String> getNameDE() {
		return Optional.fromNullable(this.nameDE);
	}

	@Override
	public Optional<String> getNameEN() {

		return Optional.fromNullable(this.nameEN);
	}

	@Override
	public Optional<String> getNameES() {
		return Optional.fromNullable(this.nameES);
	}

	@Override
	public Optional<String> getNameFR() {
		return Optional.fromNullable(this.nameFR);
	}

	public String toString() {
		return Objects.toStringHelper(this).add("super", super.toString()).add("nameDE", this.getNameDE()).add("nameEN", this.getNameEN()).add("nameES", this.getNameES()).add("nameFR", this.nameFR)
				.add("origin", this.getOriginWorldId()).toString();
	}

	@Override
	public Optional<Integer> getOriginWorldId() {
		return Optional.fromNullable(this.originWorldId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getClass().getName(), this.originWorldId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof IWorldEntity)) {
			return false;
		} else {
			final IWorldEntity worldEntity = (IWorldEntity) obj;
			return Objects.equal(this.originWorldId, worldEntity.getOriginWorldId().orNull());
		}
	}

	@Override
	public Iterable<IWVWMatchEntity> getParticipatedInMatches() {
		checkState(this.participatedInMatchesAsBlueWorld != null);
		checkState(this.participatedInMatchesAsGreenWorld != null);
		checkState(this.participatedInMatchesAsRedWorld != null);
		return Iterables.<IWVWMatchEntity> unmodifiableIterable(Iterables.<IWVWMatchEntity> concat(this.participatedInMatchesAsBlueWorld, this.participatedInMatchesAsGreenWorld,
				this.participatedInMatchesAsRedWorld));
	}

	@Override
	public List<IWVWMatchEntity> getParticipatedInMatchesAsRedWorld() {
		return Collections.<IWVWMatchEntity> unmodifiableList(this.participatedInMatchesAsRedWorld);
	}

	@Override
	public List<IWVWMatchEntity> getParticipatedInMatchesAsGreenWorld() {
		return Collections.<IWVWMatchEntity> unmodifiableList(this.participatedInMatchesAsGreenWorld);
	}

	@Override
	public List<IWVWMatchEntity> getParticipatedInMatchesAsBlueWorld() {
		return Collections.<IWVWMatchEntity> unmodifiableList(this.participatedInMatchesAsBlueWorld);
	}
}
