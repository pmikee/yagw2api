package api.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import api.service.IWVWService;
import api.service.dto.IWVWDTOFactory;
import api.service.dto.IWVWMatchDetailsDTO;
import api.service.dto.IWVWMatchesDTO;
import api.service.dto.IWVWObjectiveNameDTO;
import api.service.dto.IWorldNameDTO;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.sun.jersey.api.client.WebResource;

public class WVWService extends AbstractService implements IWVWService {
	private static final String API_VERSION = "v1";
	private static final Logger LOGGER = Logger.getLogger(WVWService.class);

	private static final URL MATCHES_URL;
	private static final URL MATCH_DETAILS_URL;
	private static final URL OBJECTIVE_NAMES_URL;
	private static final URL WORL_NAMES_URL;
	static {
		try {
			MATCHES_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/wvw/matches.json");
			MATCH_DETAILS_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/wvw/match_details.json");
			OBJECTIVE_NAMES_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/wvw/objective_names.json");
			WORL_NAMES_URL = new URL("https://api.guildwars2.com/" + API_VERSION + "/world_names.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	@Inject
	private IWVWDTOFactory wvwDTOFactory;
	private final Cache<Locale, IWorldNameDTO[]> worldNameCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build();
	private final Cache<Locale, IWVWObjectiveNameDTO[]> objectiveNameCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build();
	private final Cache<String, IWVWMatchDetailsDTO> matchDetailsCache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS).build();
	private final Cache<String, IWVWMatchesDTO> matchesCache = CacheBuilder.newBuilder().initialCapacity(1).maximumSize(1)
			.expireAfterAccess(1, TimeUnit.HOURS).build();

	public IWVWMatchesDTO retrieveAllMatches() {
		try {
			return this.matchesCache.get("", new Callable<IWVWMatchesDTO>() {
				public IWVWMatchesDTO call() throws Exception {
					final WebResource resource = CLIENT.resource(MATCHES_URL.toExternalForm());
					final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
					LOGGER.trace("Retrieved response=" + response);
					final IWVWMatchesDTO result = WVWService.this.wvwDTOFactory.createMatchesDTOfromJSON(response, WVWService.this);
					LOGGER.debug("Built result=" + result);
					return result;
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IWVWMatchesDTO.class.getSimpleName() + " from cache.", e);
			throw new IllegalStateException("Failed to retrieve " + IWVWMatchesDTO.class.getSimpleName() + " from cache.", e);
		}
	}

	public IWVWMatchDetailsDTO retrieveMatchDetails(final String id) {
		checkNotNull(id);
		try {
			return this.matchDetailsCache.get(id, new Callable<IWVWMatchDetailsDTO>() {
				public IWVWMatchDetailsDTO call() throws Exception {
					final WebResource resource = CLIENT.resource(MATCH_DETAILS_URL.toExternalForm()).queryParam("match_id", id);
					final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
					LOGGER.trace("Retrieved response=" + response);
					final IWVWMatchDetailsDTO result = WVWService.this.wvwDTOFactory.createMatchDetailsfromJSON(response, WVWService.this);
					LOGGER.debug("Built result=" + result);
					return result;
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve all " + IWVWMatchDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
			throw new IllegalStateException("Failed to retrieve all " + IWVWMatchDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
		}
	}

	public IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(final Locale locale) {
		checkNotNull(locale);
		try {
			return this.objectiveNameCache.get(locale, new Callable<IWVWObjectiveNameDTO[]>() {
				public IWVWObjectiveNameDTO[] call() throws Exception {
					final WebResource resource = CLIENT.resource(OBJECTIVE_NAMES_URL.toExternalForm()).queryParam("lang", locale.getLanguage());
					final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
					LOGGER.trace("Retrieved response=" + response);
					final IWVWObjectiveNameDTO[] result = WVWService.this.wvwDTOFactory.createObjectiveNamesFromJSON(response, WVWService.this);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Built result=" + Arrays.deepToString(result));
					}
					return result;
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve all " + IWVWObjectiveNameDTO.class.getSimpleName() + " from cache for lang=" + locale, e);
			throw new IllegalStateException("Failed to retrieve all " + IWVWObjectiveNameDTO.class.getSimpleName() + " from cache for lang=" + locale, e);
		}
	}

	public IWorldNameDTO[] retrieveAllWorldNames(final Locale locale) {
		checkNotNull(locale);
		try {
			return this.worldNameCache.get(locale, new Callable<IWorldNameDTO[]>() {
				public IWorldNameDTO[] call() throws Exception {
					checkNotNull(locale);
					final WebResource resource = CLIENT.resource(WORL_NAMES_URL.toExternalForm()).queryParam("lang", locale.getLanguage());
					final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
					LOGGER.trace("Retrieved response=" + response);
					final IWorldNameDTO[] result = WVWService.this.wvwDTOFactory.createWorldNamesFromJSON(response, WVWService.this);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Built result=" + Arrays.deepToString(result));
					}
					return result;
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for lang=" + locale, e);
			throw new IllegalStateException("Failed to retrieve all " + IWorldNameDTO.class.getSimpleName() + " from cache for lang=" + locale, e);
		}
	}

}
