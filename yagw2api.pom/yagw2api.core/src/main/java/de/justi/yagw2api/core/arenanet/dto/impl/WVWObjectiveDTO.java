package de.justi.yagw2api.core.arenanet.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWObjectiveNameDTO;
import de.justi.yagw2api.core.arenanet.service.IWVWService;

class WVWObjectiveDTO implements IWVWObjectiveDTO {
	private static final transient IWVWService SERVICE = YAGW2APICore.getInjector().getInstance(IWVWService.class);
	
	@Since(1.0)
	@SerializedName("id")
	private int id;
	@Since(1.0)
	@SerializedName("owner")
	private String owner;
	@Since(1.0)
	@SerializedName("owner_guild")
	private String guildId;
	
	public int getId() {
		return this.id;
	}

	public String getOwner() {
		return this.owner;
	}

	public String getGuildId() {
		return this.guildId;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.getName(Locale.getDefault())).add("owner", this.owner).add("guildId", this.guildId).toString();
	}

	public Optional<IWVWObjectiveNameDTO> getName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getId() > 0);
		return SERVICE.retrieveObjectiveName(locale, this.getId());
	}
}
