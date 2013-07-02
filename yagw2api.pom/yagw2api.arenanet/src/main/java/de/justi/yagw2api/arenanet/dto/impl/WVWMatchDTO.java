package de.justi.yagw2api.arenanet.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.arenanet.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.dto.IWorldNameDTO;
import de.justi.yagw2api.arenanet.service.IWVWService;

final class WVWMatchDTO implements IWVWMatchDTO {
	static final transient Logger LOGGER = Logger.getLogger(WVWMapDTO.class);
	static final transient IWVWService SERVICE = YAGW2APIArenanet.getInjector().getInstance(IWVWService.class);

	@Since(1.0)
	@SerializedName("wvw_match_id")
	private String id;
	@Since(1.0)
	@SerializedName("red_world_id")
	private int redWorldId;
	@Since(1.0)
	@SerializedName("blue_world_id")
	private int blueWorldId;
	@Since(1.0)
	@SerializedName("green_world_id")
	private int greenWorldId;
	@Since(1.0)
	@SerializedName("start_time")
	private String startTimeString = "";
	@Since(1.0)
	@SerializedName("end_time")
	private String endTimeString = "";

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("redWorldId", this.redWorldId).add("redWorld", this.getRedWorldName(YAGW2APIArenanet.getCurrentLocale()))
				.add("blueWorldId", this.blueWorldId).add("blueWorld", this.getBlueWorldName(Locale.getDefault())).add("greenWorldId", this.greenWorldId)
				.add("greenWorld", this.getGreenWorldName(Locale.getDefault())).add("start", this.getStartTime()).add("end", this.getEndTime()).toString();
	}

	@Override
	public int getRedWorldId() {
		return this.redWorldId;
	}

	@Override
	public int getGreenWorldId() {
		return this.greenWorldId;
	}

	@Override
	public int getBlueWorldId() {
		return this.blueWorldId;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Optional<IWorldNameDTO> getRedWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getRedWorldId() > 0);
		return SERVICE.retrieveWorldName(locale, this.getRedWorldId());
	}

	@Override
	public Optional<IWorldNameDTO> getGreenWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getGreenWorldId() > 0);
		return SERVICE.retrieveWorldName(locale, this.getGreenWorldId());
	}

	@Override
	public Optional<IWorldNameDTO> getBlueWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getBlueWorldId() > 0);
		return SERVICE.retrieveWorldName(locale, this.getBlueWorldId());
	}

	@Override
	public Optional<IWVWMatchDetailsDTO> getDetails() {
		checkState(this.id != null);
		return SERVICE.retrieveMatchDetails(this.id);
	}

	@Override
	public Date getStartTime() {
		if (this.startTimeString != null) {
			final DateFormat df = SERVICE.getZuluDateFormat();
			try {
				return df.parse(this.startTimeString);
			} catch (NumberFormatException | ParseException e) {
				LOGGER.debug("Failed to parse " + this.startTimeString + " using " + df + " of " + WVWMatchDTO.class.getSimpleName() + " with id=" + this.id, e);
				return null;
			}
		} else {
			LOGGER.debug("Starttime has not been initialized yet.");
			return null;
		}
	}

	@Override
	public Date getEndTime() {
		if (this.endTimeString != null) {
			final DateFormat df = SERVICE.getZuluDateFormat();
			try {
				return df.parse(this.endTimeString);
			} catch (NumberFormatException | ParseException e) {
				LOGGER.debug("Failed to parse " + this.endTimeString + " using " + df + " of " + WVWMatchDTO.class.getSimpleName() + " with id=" + this.id, e);
				return null;
			}
		} else {
			LOGGER.debug("Endtime has not been initialized yet.");
			return null;
		}
	}
}
