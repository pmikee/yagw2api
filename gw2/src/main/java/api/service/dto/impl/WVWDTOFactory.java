package api.service.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import api.service.dto.IWVWDTOFactory;
import api.service.dto.IWVWMatchDetailsDTO;
import api.service.dto.IWVWMatchesDTO;
import api.service.dto.IWVWObjectiveNameDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WVWDTOFactory implements IWVWDTOFactory {
	private static final Gson GSON = new GsonBuilder().create();
	
	public IWVWMatchesDTO createMatchesDTOfromJSON(String json) {
		return GSON.fromJson(checkNotNull(json), WVWMatchesDTO.class);
	}
	
	public IWVWMatchDetailsDTO createMatchDetailsfromJSON(String json){
		return GSON.fromJson(checkNotNull(json), WVWMatchDetailsDTO.class);
	}

	public IWVWObjectiveNameDTO[] createObjectiveNamesFromJSON(String json) {
		return GSON.fromJson(checkNotNull(json), WVWObjectiveNameDTO[].class);
	}
}
