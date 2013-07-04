package de.justi.yagw2api.gw2stats.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionsDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStatesDTO;
import de.justi.yagw2api.gw2stats.dto.IGW2StatsDTOFactory;
import de.justi.yagw2api.gw2stats.service.IGW2StatsService;
import de.justi.yagwapi.common.AbstractService;
import de.justi.yagwapi.common.utils.RetryClientFilter;

final class GW2StatsService extends AbstractService implements IGW2StatsService {
	private static final int RETRY_COUNT = 10;
	private static final Logger LOGGER = Logger.getLogger(GW2StatsService.class);

	private static final long API_STATES_CACHE_EXPIRE_MILLIS = 1000 * 15; // 15s
	private static final long API_STATE_DESCRIPTIONS_CACHE_EXPIRE_MILLIS = 1000 * 60 * 60; // 1h
	private static final URL API_STATES_URL;
	private static final URL API_STATE_DESCRIPTIONS_URL;
	static {
		try {
			API_STATES_URL = new URL("http://gw2stats.net/api/status.json");
			API_STATE_DESCRIPTIONS_URL = new URL("http://gw2stats.net/api/status_codes.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// injections
	private IGW2StatsDTOFactory gw2statsDTOFactory;

	@Inject
	public GW2StatsService(IGW2StatsDTOFactory gw2statsDTOFactory) {
		checkNotNull(gw2statsDTOFactory);
		this.gw2statsDTOFactory = gw2statsDTOFactory;
	}

	private final Cache<String, IAPIStatesDTO> apieStatesCache = CacheBuilder.newBuilder().expireAfterWrite(API_STATES_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
	private final Cache<String, IAPIStateDescriptionsDTO> apieStateDescriptionsCache = CacheBuilder.newBuilder().expireAfterWrite(API_STATE_DESCRIPTIONS_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS)
			.build();

	@Override
	public IAPIStatesDTO retrieveAPIStates() {
		try {
			return this.apieStatesCache.get("", new Callable<IAPIStatesDTO>() {
				@Override
				public IAPIStatesDTO call() throws Exception {
					final WebResource resource = CLIENT.resource(API_STATES_URL.toExternalForm());
					resource.addFilter(new RetryClientFilter(RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class).replaceAll(Pattern.quote("\\"), "");
						LOGGER.trace("Retrieved response=" + response);
						final IAPIStatesDTO result = GW2StatsService.this.gw2statsDTOFactory.newAPIStatesOf(response);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Built result=" + result);
						}
						return result;
					} catch (ClientHandlerException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return null;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IAPIStatesDTO.class.getSimpleName() + " from cache.", e);
			throw new IllegalStateException("Failed to retrieve " + IAPIStatesDTO.class.getSimpleName() + " from cache.", e);
		}

	}

	@Override
	public IAPIStateDescriptionsDTO retrieveAPIStateDescriptions() {
		try {
			return this.apieStateDescriptionsCache.get("", new Callable<IAPIStateDescriptionsDTO>() {
				@Override
				public IAPIStateDescriptionsDTO call() throws Exception {
					final WebResource resource = CLIENT.resource(API_STATE_DESCRIPTIONS_URL.toExternalForm());
					resource.addFilter(new RetryClientFilter(RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IAPIStateDescriptionsDTO result = GW2StatsService.this.gw2statsDTOFactory.newAPIStateDescriptionsOf(response);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Built result=" + result);
						}
						return result;
					} catch (ClientHandlerException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return null;
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IAPIStatesDTO.class.getSimpleName() + " from cache.", e);
			throw new IllegalStateException("Failed to retrieve " + IAPIStatesDTO.class.getSimpleName() + " from cache.", e);
		}
	}

	@Override
	public Optional<IAPIStateDescriptionDTO> retrieveAPIStateDescription(String state) {
		return this.retrieveAPIStateDescriptions().getDescriptionOfState(state);
	}
}
