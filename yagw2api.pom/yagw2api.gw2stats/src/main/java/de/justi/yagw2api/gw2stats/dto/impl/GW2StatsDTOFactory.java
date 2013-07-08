package de.justi.yagw2api.gw2stats.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionDTO;
import de.justi.yagw2api.gw2stats.dto.IGW2StatsDTOFactory;

public class GW2StatsDTOFactory implements IGW2StatsDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(GW2StatsDTOFactory.class);

	private Gson createGSON() {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setDateFormat("yyyy-MM-dd HH:mm:ss zzz").setVersion(1.0).create();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, IAPIStateDTO> newAPIStatesOf(String json) {
		LOGGER.trace("Going to built " + Map.class.getSimpleName() + " of " + IAPIStateDTO.class.getSimpleName() + " out of json string of length=" + json.length());
		Map<String, IAPIStateDTO> result;
		try {
			result = (Map<String, IAPIStateDTO>) this.createGSON().fromJson(checkNotNull(json), APIStateDTOResultWrapper.class).getResult();
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, IAPIStateDescriptionDTO> newAPIStateDescriptionsOf(String json) {
		LOGGER.trace("Going to built " + Map.class.getSimpleName() + " of " + IAPIStateDescriptionDTO.class.getSimpleName() + " out of json string of length=" + json.length());
		Map<String, IAPIStateDescriptionDTO> result;
		try {
			result = (Map<String, IAPIStateDescriptionDTO>) this.createGSON().fromJson(checkNotNull(json), APIStateDescriptionDTOResultWrapper.class).getResult();
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

}
