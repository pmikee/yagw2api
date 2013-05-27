package de.justi.gw2.api.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;



import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.gw2.api.dto.IWVWObjectiveDTO;
import de.justi.gw2.api.dto.IWVWObjectiveNameDTO;
import de.justi.gw2.api.service.IWVWService;
import de.justi.gw2.utils.InjectionHelper;

class WVWObjectiveDTO implements IWVWObjectiveDTO {
	private static final transient IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	
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
