package de.justi.yagw2api.gw2stats.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-GW2Stats
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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


import static com.google.common.base.Preconditions.checkState;

import java.util.Date;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.IAPIStateDTO;
import de.justi.yagw2api.gw2stats.IAPIStateDescriptionDTO;
import de.justi.yagw2api.gw2stats.IGW2StatsService;
import de.justi.yagw2api.gw2stats.YAGW2APIGW2Stats;

final class APIStateDTO implements IAPIStateDTO {
	static final transient Logger LOGGER = Logger.getLogger(APIStateDTO.class);
	static final transient IGW2StatsService SERVICE = YAGW2APIGW2Stats.INSTANCE.getGW2StatsService();

	@SerializedName("status")
	@Since(1.0)
	private String state;

	@SerializedName("ping")
	@Since(1.0)
	private int ping;

	@SerializedName("retrieve")
	@Since(1.0)
	private int retrieve;

	@SerializedName("records")
	@Since(1.0)
	private int records;

	@SerializedName("time")
	@Since(1.0)
	private Date time;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("state", this.state).add("ping", this.ping).add("retrieve", this.retrieve).add("records", this.records).add("time", this.time)
				.add("description", this.getDescription()).toString();
	}

	/**
	 * @return the status
	 */
	@Override
	public final String getStatus() {
		return state;
	}

	/**
	 * @return the ping
	 */
	@Override
	public final int getPing() {
		return ping;
	}

	/**
	 * @return the retrieve
	 */
	@Override
	public final int getRetrieve() {
		return retrieve;
	}

	/**
	 * @return the records
	 */
	@Override
	public final int getRecords() {
		return records;
	}

	/**
	 * @return the time
	 */
	@Override
	public final Date getTime() {
		return time;
	}

	@Override
	public Optional<IAPIStateDescriptionDTO> getDescription() {
		checkState(this.state != null);
		return SERVICE.retrieveAPIStateDescription(this.state);
	}
}
