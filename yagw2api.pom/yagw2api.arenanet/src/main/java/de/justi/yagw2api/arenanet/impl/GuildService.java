package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.arenanet.IGuildDTOFactory;
import de.justi.yagw2api.arenanet.IGuildDetailsDTO;
import de.justi.yagw2api.arenanet.IGuildService;
import de.justi.yagwapi.common.utils.RetryClientFilter;

final class GuildService implements IGuildService {
	private static final Logger LOGGER = Logger.getLogger(GuildService.class);
	private static final long GUILD_DETAILS_CACHE_EXPIRE_MILLIS = 1000 * 60 * 5; // 5m

	private static final URL GUILD_DETAILS_URL;
	static {
		try {
			GUILD_DETAILS_URL = new URL("https://api.guildwars2.com/" + ServiceUtils.API_VERSION + "/guild_details.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}

	// FIELDS
	private final Cache<String, Optional<IGuildDetailsDTO>> guildDetailsCache = CacheBuilder.newBuilder().expireAfterWrite(GUILD_DETAILS_CACHE_EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build();
	private final IGuildDTOFactory guildDTOFactory;

	// METHODS
	@Inject
	public GuildService(IGuildDTOFactory guildDTOFactory) {
		this.guildDTOFactory = checkNotNull(guildDTOFactory);
	}

	@Override
	public Optional<IGuildDetailsDTO> retrieveGuildDetails(final String id) {
		checkNotNull(id);
		try {
			return this.guildDetailsCache.get(id, new Callable<Optional<IGuildDetailsDTO>>() {
				@Override
				public Optional<IGuildDetailsDTO> call() throws Exception {
					final WebResource resource = ServiceUtils.REST_CLIENT.resource(GUILD_DETAILS_URL.toExternalForm()).queryParam("guild_id", id);
					resource.addFilter(new RetryClientFilter(ServiceUtils.REST_RETRY_COUNT));
					final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
					try {
						final String response = builder.get(String.class);
						LOGGER.trace("Retrieved response=" + response);
						final IGuildDetailsDTO result = GuildService.this.guildDTOFactory.newGuildDetailsOf(response);
						LOGGER.debug("Built result=" + result);
						return Optional.of(result);
					} catch (ClientHandlerException | UniformInterfaceException e) {
						LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
						return Optional.absent();
					}
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve " + IGuildDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
			throw new IllegalStateException("Failed to retrieve " + IGuildDetailsDTO.class.getSimpleName() + " from cache for id=" + id, e);
		}
	}
}
