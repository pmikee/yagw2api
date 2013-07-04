package de.justi.yagw2api.gw2stats.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionsDTO;

class APIStateDescriptionsDTO implements IAPIStateDescriptionsDTO {
	static final transient Logger LOGGER = Logger.getLogger(APIStateDescriptionsDTO.class);
	@SerializedName("OK")
	@Since(1.0)
	private APIStateDescriptionDTO okDescription;

	@SerializedName("UNREACHABLE")
	@Since(1.0)
	private APIStateDescriptionDTO unreachableDescription;

	@SerializedName("DOWN")
	@Since(1.0)
	private APIStateDescriptionDTO downDescription;

	@SerializedName("PARTIAL")
	@Since(1.0)
	private APIStateDescriptionDTO partialDescription;

	@SerializedName("INCREASING PING")
	@Since(1.0)
	private APIStateDescriptionDTO increasingPingDescription;

	@SerializedName("HIGH PING")
	@Since(1.0)
	private APIStateDescriptionDTO highPingDescription;

	@SerializedName("SLOW RETRIEVE")
	@Since(1.0)
	private APIStateDescriptionDTO slowRetrieveDescription;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("ok", this.okDescription).add("unreachable", this.unreachableDescription).add("down", this.downDescription).add("partial", this.partialDescription)
				.add("increasing ping", this.increasingPingDescription).add("hight ping", this.highPingDescription).add("slow retrieve", this.slowRetrieveDescription).toString();
	}

	/**
	 * @return the okDescription
	 */
	@Override
	public final IAPIStateDescriptionDTO getOkDescription() {
		return okDescription;
	}

	/**
	 * @return the unreachableDescription
	 */
	@Override
	public final IAPIStateDescriptionDTO getUnreachableDescription() {
		return unreachableDescription;
	}

	/**
	 * @return the downDescription
	 */
	@Override
	public final IAPIStateDescriptionDTO getDownDescription() {
		return downDescription;
	}

	/**
	 * @return the partialDescription
	 */
	@Override
	public final IAPIStateDescriptionDTO getPartialDescription() {
		return partialDescription;
	}

	/**
	 * @return the increasingPingDescription
	 */
	@Override
	public final IAPIStateDescriptionDTO getIncreasingPingDescription() {
		return increasingPingDescription;
	}

	/**
	 * @return the highPingDescription
	 */
	@Override
	public final IAPIStateDescriptionDTO getHighPingDescription() {
		return highPingDescription;
	}

	/**
	 * @return the slowRetrieveDescription
	 */
	@Override
	public final IAPIStateDescriptionDTO getSlowRetrieveDescription() {
		return slowRetrieveDescription;
	}

	@Override
	public Optional<IAPIStateDescriptionDTO> getDescriptionOfState(String state) {
		switch (checkNotNull(state).toUpperCase()) {
			case "OK":
				return Optional.of(this.getOkDescription());
			case "UNREACHABLE":
				return Optional.of(this.getUnreachableDescription());
			case "DOWN":
				return Optional.of(this.getDownDescription());
			case "PARTIAL":
				return Optional.of(this.getDownDescription());
			case "INCREASING PING":
				return Optional.of(this.getIncreasingPingDescription());
			case "HIGH PING":
				return Optional.of(this.getHighPingDescription());
			case "SLOW RETRIEVE":
				return Optional.of(this.getSlowRetrieveDescription());
			default:
				LOGGER.warn("Unknown state: " + state);
				return Optional.absent();
		}
	}
}
