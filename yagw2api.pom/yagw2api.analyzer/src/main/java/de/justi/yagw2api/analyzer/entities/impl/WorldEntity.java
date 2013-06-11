package de.justi.yagw2api.analyzer.entities.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.AbstractEntity;
import de.justi.yagw2api.analyzer.entities.IWorldEntity;
import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.wrapper.model.IWorld;

@Entity(name = "world")
class WorldEntity extends AbstractEntity implements IWorldEntity {
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

	public WorldEntity() {
		this.nameDE = null;
		this.originWorldId = null;
	}

	@Override
	public boolean synchronizeWithModel(IWorld model) {
		checkNotNull(model);
		if (this.originWorldId == null || model.getId() == this.originWorldId) {
			// compatible ids
			this.originWorldId = model.getId();
			if(model.getName().isPresent()) {
				this.setName(YAGW2APICore.getCurrentLocale(), model.getName().get());
			}
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
}
