package de.justi.yagw2api.arenanet.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.arenanet.dto.IGuildDetailsDTO;
import de.justi.yagw2api.arenanet.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.arenanet.dto.IWVWObjectiveNameDTO;
import de.justi.yagw2api.arenanet.service.IWVWService;

final class WVWObjectiveDTO implements IWVWObjectiveDTO {
	private static final transient IWVWService SERVICE = YAGW2APIArenanet.getInjector().getInstance(IWVWService.class);

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
		return this.owner;
	}

	@Override
	public String getGuildId() {
		return this.guildId;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.getName(YAGW2APIArenanet.getCurrentLocale())).add("owner", this.owner).add("guildId", this.guildId).toString();
	}

	@Override
	public Optional<IWVWObjectiveNameDTO> getName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getId() > 0);
		return SERVICE.retrieveObjectiveName(locale, this.getId());
	}

	@Override
	public Optional<IGuildDetailsDTO> getGuildDetails() {
		if (this.guildId == null) {
			return Optional.absent();
		} else {
			return SERVICE.retrieveGuildDetails(this.guildId);
		}
	}
}
