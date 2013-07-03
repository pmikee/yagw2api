package de.justi.yagw2api.gw2stats.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IStatusDTO;

final class StatusDTO implements IStatusDTO {
	@SerializedName("status")
	@Since(1.0)
	private String status;

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
	private String time;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("status", this.status).add("ping", this.ping).add("retrieve", this.retrieve).add("records", this.records).add("time", this.time).toString();
	}

	/**
	 * @return the status
	 */
	@Override
	public final String getStatus() {
		return status;
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
	public final String getTime() {
		return time;
	}
}
