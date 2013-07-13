package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IGuildDetailsDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveNameDTO;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;

final class WVWObjectiveDTO implements IWVWObjectiveDTO {

	@Since(1.0)
	@SerializedName("id")
	private int id;
	@Since(1.0)
	@SerializedName("owner")
	private String owner;
	@Since(1.0)
	@SerializedName("owner_guild")
	private String guildId;

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getOwner() {
		return this.owner == null ? null : this.owner.toUpperCase();
	}

	@Override
	public String getGuildId() {
		return this.guildId;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.getName(YAGW2APIArenanet.getInstance().getCurrentLocale())).add("owner", this.owner).add("guildId", this.guildId)
				.toString();
	}

	@Override
	public Optional<IWVWObjectiveNameDTO> getName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getId() > 0);
		return YAGW2APIArenanet.getInstance().getWVWService().retrieveObjectiveName(locale, this.getId());
	}

	@Override
	public Optional<IGuildDetailsDTO> getGuildDetails() {
		if (this.guildId == null) {
			return Optional.absent();
		} else {
			return YAGW2APIArenanet.getInstance().getGuildService().retrieveGuildDetails(this.guildId);
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
		result = (prime * result) + ((guildId == null) ? 0 : guildId.hashCode());
		result = (prime * result) + id;
		result = (prime * result) + ((owner == null) ? 0 : owner.hashCode());
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
		WVWObjectiveDTO other = (WVWObjectiveDTO) obj;
		if (guildId == null) {
			if (other.guildId != null) {
				return false;
			}
		} else if (!guildId.equals(other.guildId)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		return true;
	}

}
