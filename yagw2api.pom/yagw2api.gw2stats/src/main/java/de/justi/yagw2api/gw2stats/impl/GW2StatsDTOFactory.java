package de.justi.yagw2api.gw2stats.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.gw2stats.IAPIStateDTO;
import de.justi.yagw2api.gw2stats.IAPIStateDescriptionDTO;
import de.justi.yagw2api.gw2stats.IGW2StatsDTOFactory;

final class GW2StatsDTOFactory implements IGW2StatsDTOFactory {
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

	@Override
	public Map<String, IAPIStateDescriptionDTO> newAPIStateDescriptionsOf(String json) {
		LOGGER.trace("Going to built " + Map.class.getSimpleName() + " of " + IAPIStateDescriptionDTO.class.getSimpleName() + " out of json string of length=" + json.length());
		Map<String, IAPIStateDescriptionDTO> result;
		try {
			// the following roundtrip is required, because gson handling for interfaces suxx
			final Map<String, IAPIStateDescriptionDTO> buffer = Maps.newHashMap();
			buffer.putAll(this.createGSON().fromJson(checkNotNull(json), APIStateDescriptionDTOResultWrapper.class).getResult());
			result = ImmutableMap.copyOf(buffer);
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}
}