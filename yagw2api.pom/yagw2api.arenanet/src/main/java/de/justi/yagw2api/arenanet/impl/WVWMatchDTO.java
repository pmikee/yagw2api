package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IArenanet;
import de.justi.yagw2api.arenanet.IWVWMatchDTO;
import de.justi.yagw2api.arenanet.IWVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.IWorldNameDTO;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;

final class WVWMatchDTO implements IWVWMatchDTO {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(WVWMapDTO.class);
	private static final transient IArenanet ARENANET = YAGW2APIArenanet.INSTANCE;

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
				.add("redWorld", this.getRedWorldName(YAGW2APIArenanet.getInstance().getCurrentLocale())).add("blueWorldId", this.blueWorldId)
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
	public Optional<IWorldNameDTO> getRedWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getRedWorldId() > 0);
		return YAGW2APIArenanet.getInstance().getWorldService().retrieveWorldName(locale, this.getRedWorldId());
	}

	@Override
	public Optional<IWorldNameDTO> getGreenWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getGreenWorldId() > 0);
		return YAGW2APIArenanet.getInstance().getWorldService().retrieveWorldName(locale, this.getGreenWorldId());
	}

	@Override
	public Optional<IWorldNameDTO> getBlueWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getBlueWorldId() > 0);
		return YAGW2APIArenanet.getInstance().getWorldService().retrieveWorldName(locale, this.getBlueWorldId());
	}

	@Override
	public Optional<IWVWMatchDetailsDTO> getDetails() {
		checkState(this.id != null);
		return YAGW2APIArenanet.getInstance().getWVWService().retrieveMatchDetails(this.id);
	}

	// FIXME make this return optional
	@Override
	public LocalDateTime getStartTime() {
		if (this.startTimeString != null) {
			try {
				return ServiceUtils.parseZULUTimestampString(this.startTimeString);
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

				return ServiceUtils.parseZULUTimestampString(this.endTimeString);
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
		result = (prime * result) + blueWorldId;
		result = (prime * result) + ((endTimeString == null) ? 0 : endTimeString.hashCode());
		result = (prime * result) + greenWorldId;
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result) + redWorldId;
		result = (prime * result) + ((startTimeString == null) ? 0 : startTimeString.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WVWMatchDTO other = (WVWMatchDTO) obj;
		if (blueWorldId != other.blueWorldId) {
			return false;
		}
		if (endTimeString == null) {
			if (other.endTimeString != null) {
				return false;
			}
		} else if (!endTimeString.equals(other.endTimeString)) {
			return false;
		}
		if (greenWorldId != other.greenWorldId) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (redWorldId != other.redWorldId) {
			return false;
		}
		if (startTimeString == null) {
			if (other.startTimeString != null) {
				return false;
			}
		} else if (!startTimeString.equals(other.startTimeString)) {
			return false;
		}
		return true;
	}
}
