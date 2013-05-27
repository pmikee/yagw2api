package de.justi.gw2.api.dto.impl;

import java.util.Arrays;


import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.gw2.api.dto.IWVWMatchDTO;
import de.justi.gw2.api.dto.IWVWMatchesDTO;

class WVWMatchesDTO implements IWVWMatchesDTO {

	@Since(1.0)
	@SerializedName("wvw_matches")
	private WVWMatchDTO[] matches;

	public String toString() {
		return Objects.toStringHelper(this).add("matches", Arrays.deepToString(this.matches)).toString();
	}

	public IWVWMatchDTO[] getMatches() {
		return this.matches;
	}
}
