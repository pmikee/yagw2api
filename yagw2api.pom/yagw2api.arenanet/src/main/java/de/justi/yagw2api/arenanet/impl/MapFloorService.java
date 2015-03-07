package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple3;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.IMapDTOFactory;
import de.justi.yagw2api.arenanet.IMapFloorDTO;
import de.justi.yagw2api.arenanet.IMapFloorService;
import de.justi.yagwapi.common.RetryClientFilter;

final class MapFloorService implements IMapFloorService {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(MapFloorService.class);
	private static final long CACHE_EXPIRE_MILLIS = TimeUnit.HOURS.toMillis(12);
	private static final URL MAP_FLOOR_URL;
	static {
		try {
			MAP_FLOOR_URL = new URL("https://api.guildwars2.com/" + ServiceUtils.API_VERSION + "/map_floor.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// FIELDS
	private final IMapDTOFactory mapDTOFactory;
	private final LoadingCache<Tuple3<Locale, Integer, Integer>, Optional<IMapFloorDTO>> mapFloorCache = CacheBuilder.newBuilder()
			.expireAfterWrite(CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build(new CacheLoader<Tuple3<Locale, Integer, Integer>, Optional<IMapFloorDTO>>() {
				@Override
				public Optional<IMapFloorDTO> load(final Tuple3<Locale, Integer, Integer> key) throws Exception {
					final WebResource resource = ServiceUtils.REST_CLIENT.resource(MAP_FLOOR_URL.toExternalForm()).queryParam("continent_id ", key._2().toString())
							.queryParam("floor", key._3().toString()).queryParam("lang", key._1().toLanguageTag());
					resource.addFilter(new RetryClientFilter(ServiceUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IMapFloorDTO result = MapFloorService.this.mapDTOFactory.newMapFloorOf(response);
						LOGGER.debug("Built result=" + result);
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.error("Exception thrown while quering {}", resource, e);
						return Optional.absent();
					}
				}
			});

	// CONSTRUCTOR
	@Inject
	public MapFloorService(final IMapDTOFactory mapDTOFactory) {
		this.mapDTOFactory = checkNotNull(mapDTOFactory);
	}

	// METHODS
	@Override
	public Optional<IMapFloorDTO> retrieveMapFloor(final int continentId, final int floor, final Locale lang) {
		checkNotNull(lang, "missing lang");
		try {
			return this.mapFloorCache.get(new Tuple3<Locale, Integer, Integer>(lang, continentId, floor));
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve {} from cache for continentId={}, floor={}, lang={}", IMapFloorDTO.class, continentId, floor, lang, e);
			throw new IllegalStateException("Failed to retrieve " + IMapFloorDTO.class.getSimpleName() + " from cache for continentId=" + continentId + ", floor=" + floor
					+ ", lang=" + lang, e);
		}
	}
}
