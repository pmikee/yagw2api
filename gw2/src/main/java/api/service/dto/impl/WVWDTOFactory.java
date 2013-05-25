package api.service.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.apache.log4j.Logger;

import api.service.IWVWService;
import api.service.dto.IWVWDTOFactory;
import api.service.dto.IWVWMatchDetailsDTO;
import api.service.dto.IWVWMatchesDTO;
import api.service.dto.IWVWObjectiveNameDTO;
import api.service.dto.IWorldNameDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class WVWDTOFactory implements IWVWDTOFactory {
	private static final Logger LOGGER = Logger.getLogger(WVWDTOFactory.class);

	public WVWDTOFactory() {

	}

	private Gson createGSON(final IWVWService service) {
		return new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setVersion(1.0)
				.registerTypeAdapter(WVWMatchDTO.class, new InstanceCreator<WVWMatchDTO>() {
					public WVWMatchDTO createInstance(Type type) {
						return new WVWMatchDTO(service);
					}
				}).registerTypeAdapter(WVWObjectiveDTO.class, new InstanceCreator<WVWObjectiveDTO>() {
					public WVWObjectiveDTO createInstance(Type type) {
						return new WVWObjectiveDTO(service);
					}
				}).registerTypeAdapter(WVWMatchDetailsDTO.class, new InstanceCreator<WVWMatchDetailsDTO>() {
					public WVWMatchDetailsDTO createInstance(Type type) {
						return new WVWMatchDetailsDTO(service);
					}
				}).create();
	}

	public IWVWMatchesDTO createMatchesDTOfromJSON(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built " + IWVWMatchesDTO.class.getSimpleName() + " using service=" + service);
		final WVWMatchesDTO result = this.createGSON(service).fromJson(checkNotNull(json), WVWMatchesDTO.class);
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	public IWVWMatchDetailsDTO createMatchDetailsfromJSON(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built " + IWVWMatchDetailsDTO.class.getSimpleName() + " using service=" + service);
		final WVWMatchDetailsDTO result = this.createGSON(service).fromJson(checkNotNull(json), WVWMatchDetailsDTO.class);
		checkState(result != null);
		LOGGER.debug("Built " + result);
		return result;
	}

	public IWVWObjectiveNameDTO[] createObjectiveNamesFromJSON(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built all " + IWVWObjectiveNameDTO.class.getSimpleName() + " using service=" + service);
		final WVWObjectiveNameDTO[] result = this.createGSON(service).fromJson(checkNotNull(json), WVWObjectiveNameDTO[].class);
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built " + Arrays.deepToString(result));
		}
		return result;
	}

	public IWorldNameDTO[] createWorldNamesFromJSON(String json, IWVWService service) {
		checkNotNull(service);
		LOGGER.trace("Going to built " + IWorldNameDTO.class.getSimpleName() + " using service=" + service);
		final WorldNameDTO[] result = this.createGSON(service).fromJson(checkNotNull(json), WorldNameDTO[].class);
		checkState(result != null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Built " + Arrays.deepToString(result));
		}
		return result;
	}
}
