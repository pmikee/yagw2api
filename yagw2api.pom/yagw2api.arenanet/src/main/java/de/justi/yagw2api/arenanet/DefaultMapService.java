package de.justi.yagw2api.arenanet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.dto.map.MapDTOFactory;
import de.justi.yagw2api.arenanet.dto.map.MapsDTO;
import de.justi.yagwapi.common.RetryClientFilter;

final class DefaultMapService implements MapService {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMapService.class);
	private static final URL MAPS_URL;
	static {
		try {
			MAPS_URL = new URL("https://api.guildwars2.com/" + ArenanetUtils.API_VERSION + "/maps.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// FIELDS
	private final MapDTOFactory mapDTOFactory;
	private final LoadingCache<Locale, Optional<MapsDTO>> mapsCache = CacheBuilder.newBuilder().build(new CacheLoader<Locale, Optional<MapsDTO>>() {
		@Override
		public Optional<MapsDTO> load(final Locale key) throws Exception {
			final WebResource resource = ArenanetUtils.REST_CLIENT.resource(MAPS_URL.toExternalForm());
			resource.addFilter(new RetryClientFilter(ArenanetUtils.REST_RETRY_COUNT));
			final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
			try {
				final String response = builder.get(String.class);
				LOGGER.trace("Retrieved response={}", response);
				final MapsDTO result = DefaultMapService.this.mapDTOFactory.newMapsOf(response);
				LOGGER.debug("Built result={}", result);
				return Optional.of(result);
			} catch (ClientHandlerException | UniformInterfaceException e) {
				LOGGER.error("Exception thrown while quering {}", resource, e);
				return Optional.absent();
			}
		}
	});

	// CONSTRUCTOR
	@Inject
	public DefaultMapService(final MapDTOFactory mapDTOFactory) {
		this.mapDTOFactory = checkNotNull(mapDTOFactory, "missing mapDTOFactory");
	}

	// METHODS
	@Override
	public Optional<MapsDTO> retrieveAllMaps(final Locale locale) {
		checkNotNull(locale, "missing locale");
		try {
			return this.mapsCache.get(locale);
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve {} from cache.", MapsDTO.class, e);
			throw new IllegalStateException("Failed to retrieve " + MapsDTO.class.getSimpleName() + " from cache.", e);
		}
	}

}
