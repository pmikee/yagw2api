package de.justi.yagw2api.gw2stats.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionsDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStatesDTO;
import de.justi.yagw2api.gw2stats.dto.IGW2StatsDTOFactory;

public class GW2StatsDTOFactoryImpl implements IGW2StatsDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(GW2StatsDTOFactoryImpl.class);

	private Gson createGSON() {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0).create();
	}

	@Override
	public IAPIStatesDTO newAPIStatesOf(String json) {
		LOGGER.trace("Going to built " + IAPIStatesDTO.class.getSimpleName() + " out of json string of length=" + json.length());
		IAPIStatesDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), ResultWrapper.class).getAPIStates();
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	@Override
	public IAPIStateDescriptionsDTO newAPIStateDescriptionsOf(String json) {
		LOGGER.trace("Going to built " + IAPIStateDescriptionsDTO.class.getSimpleName() + " out of json string of length=" + json.length());
		IAPIStateDescriptionsDTO result;
		try {
			result = this.createGSON().fromJson(checkNotNull(json), ResultWrapper.class).getAPIStateDescriptions();
		} catch (JsonSyntaxException e) {
			result = null;
			LOGGER.fatal("Invalid response: " + json, e);
		}
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

}
