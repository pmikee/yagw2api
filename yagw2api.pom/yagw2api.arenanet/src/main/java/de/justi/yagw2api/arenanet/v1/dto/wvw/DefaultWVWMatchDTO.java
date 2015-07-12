package de.justi.yagw2api.arenanet.v1.dto.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.v1.Arenanet;
import de.justi.yagw2api.arenanet.v1.ArenanetUtils;
import de.justi.yagw2api.arenanet.v1.YAGW2APIArenanetV1;
import de.justi.yagw2api.arenanet.v1.dto.world.WorldNameDTO;

final class DefaultWVWMatchDTO implements WVWMatchDTO {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(DefaultWVWMapDTO.class);
	private static final transient Arenanet ARENANET = YAGW2APIArenanetV1.INSTANCE;

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
		return MoreObjects.toStringHelper(this).add("id", this.id).add("redWorldId", this.redWorldId)
				.add("redWorld", this.getRedWorldName(YAGW2APIArenanetV1.getInstance().getCurrentLocale())).add("blueWorldId", this.blueWorldId)
				.add("blueWorld", this.getBlueWorldName(Locale.getDefault())).add("greenWorldId", this.greenWorldId).add("greenWorld", this.getGreenWorldName(Locale.getDefault()))
				.add("start", this.getStartTime()).add("end", this.getEndTime()).toString();
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
	public Optional<WorldNameDTO> getRedWorldName(final Locale locale) {
		checkNotNull(locale);
		checkState(this.getRedWorldId() > 0);
		return YAGW2APIArenanetV1.getInstance().getWorldService().retrieveWorldName(locale, this.getRedWorldId());
	}

	@Override
	public Optional<WorldNameDTO> getGreenWorldName(final Locale locale) {
		checkNotNull(locale);
		checkState(this.getGreenWorldId() > 0);
		return YAGW2APIArenanetV1.getInstance().getWorldService().retrieveWorldName(locale, this.getGreenWorldId());
	}

	@Override
	public Optional<WorldNameDTO> getBlueWorldName(final Locale locale) {
		checkNotNull(locale);
		checkState(this.getBlueWorldId() > 0);
		return YAGW2APIArenanetV1.getInstance().getWorldService().retrieveWorldName(locale, this.getBlueWorldId());
	}

	@Override
	public Optional<WVWMatchDetailsDTO> getDetails() {
		checkState(this.id != null);
		return YAGW2APIArenanetV1.getInstance().getWVWService().retrieveMatchDetails(this.id);
	}

	// FIXME make this return optional
	@Override
	public LocalDateTime getStartTime() {
		if (this.startTimeString != null) {
			try {
				return ArenanetUtils.parseZULUTimestampString(this.startTimeString);
			} catch (ParseException e) {
				LOGGER.warn("Invalid startTimeString: {}", this.startTimeString);
				return null;
			}
		} else {
			LOGGER.debug("Starttime has not been initialized yet.");
			return null;
		}
	}

	// FIXME make this return optional
	@Override
	public LocalDateTime getEndTime() {
		if (this.endTimeString != null) {
			try {

				return ArenanetUtils.parseZULUTimestampString(this.endTimeString);
			} catch (ParseException e) {
				LOGGER.warn("Invalid endTimeString: {}", this.startTimeString);
				return null;
			}
		} else {
			LOGGER.debug("Endtime has not been initialized yet.");
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.blueWorldId;
		result = (prime * result) + ((this.endTimeString == null) ? 0 : this.endTimeString.hashCode());
		result = (prime * result) + this.greenWorldId;
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		result = (prime * result) + this.redWorldId;
		result = (prime * result) + ((this.startTimeString == null) ? 0 : this.startTimeString.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		DefaultWVWMatchDTO other = (DefaultWVWMatchDTO) obj;
		if (this.blueWorldId != other.blueWorldId) {
			return false;
		}
		if (this.endTimeString == null) {
			if (other.endTimeString != null) {
				return false;
			}
		} else if (!this.endTimeString.equals(other.endTimeString)) {
			return false;
		}
		if (this.greenWorldId != other.greenWorldId) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.redWorldId != other.redWorldId) {
			return false;
		}
		if (this.startTimeString == null) {
			if (other.startTimeString != null) {
				return false;
			}
		} else if (!this.startTimeString.equals(other.startTimeString)) {
			return false;
		}
		return true;
	}
}
