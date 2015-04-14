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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.ObjectTypeConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import de.justi.yagw2api.analyzer.IWVWMatchEntity;
import de.justi.yagw2api.analyzer.IWorldEntity;
import de.justi.yagw2api.analyzer.utils.LocaleConverter;
import de.justi.yagw2api.arenanet.Arenanet;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.domain.world.World;
import de.justi.yagw2api.wrapper.domain.world.WorldLocationType;
import de.justi.yagw2api.wrapper.domain.world.DefaultWorldLocationType;

@ObjectTypeConverters({ @ObjectTypeConverter(name = "WorldLocationTypeConverter", objectType = DefaultWorldLocationType.class, dataType = String.class, conversionValues = {
		@ConversionValue(objectValue = "NORTH_AMERICA", dataValue = "NA"), @ConversionValue(objectValue = "EUROPE", dataValue = "EU") }) })
@Converters({ @Converter(converterClass = LocaleConverter.class, name = "LocaleConverter") })
@Entity(name = "world")
public final class WorldEntity extends AbstractEntity implements IWorldEntity {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldEntity.class);
	private static final Arenanet ARENANET = YAGW2APIArenanet.INSTANCE;

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
	private WorldLocationType location;

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
	public boolean addParticipatedAsRedInMatch(final IWVWMatchEntity match) {
		checkNotNull(match);
		if (this.participatedInMatchesAsBlueWorld.contains(match) || this.participatedInMatchesAsGreenWorld.contains(match) || this.participatedInMatchesAsRedWorld.contains(match)) {
			return false;
		} else {
			return this.participatedInMatchesAsRedWorld.add((WVWMatchEntity) match);
		}
	}

	@Override
	public boolean addParticipatedAsBlueInMatch(final IWVWMatchEntity match) {
		checkNotNull(match);
		if (this.participatedInMatchesAsBlueWorld.contains(match) || this.participatedInMatchesAsGreenWorld.contains(match) || this.participatedInMatchesAsRedWorld.contains(match)) {
			return false;
		} else {
			return this.participatedInMatchesAsBlueWorld.add((WVWMatchEntity) match);
		}
	}

	@Override
	public boolean addParticipatedAsGreenInMatch(final IWVWMatchEntity match) {
		checkNotNull(match);
		if (this.participatedInMatchesAsBlueWorld.contains(match) || this.participatedInMatchesAsGreenWorld.contains(match) || this.participatedInMatchesAsRedWorld.contains(match)) {
			return false;
		} else {
			return this.participatedInMatchesAsGreenWorld.add((WVWMatchEntity) match);
		}
	}

	@Override
	public WorldLocationType getLocation() {
		return this.location;
	}

	@Override
	public Optional<Locale> getWorldLocale() {
		return Optional.fromNullable(this.worldLocale);
	}

	@Override
	public boolean synchronizeWithModel(final World model) {
		checkNotNull(model);
		if ((this.originWorldId == null) || (model.getId() == this.originWorldId)) {
			// compatible ids
			this.originWorldId = model.getId();
			if (model.getName().isPresent()) {
				this.setName(ARENANET.getCurrentLocale(), model.getName().get());
			}
			this.worldLocale = model.getWorldLocale().orNull();
			this.location = model.getWorldLocation();

			return true;
		} else {
			// incompatible ids
			return false;
		}
	}

	private void setName(final Locale locale, final String name) {
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
	public Optional<String> getName(final Locale locale) {
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
		return this.getName(ARENANET.getCurrentLocale());
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

	@Override
	public ToStringHelper toStringHelper() {
		return super.toStringHelper().add("nameDE", this.getNameDE()).add("nameEN", this.getNameEN()).add("nameES", this.getNameES()).add("nameFR", this.nameFR)
				.add("origin", this.getOriginWorldId());
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
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof IWorldEntity)) {
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
