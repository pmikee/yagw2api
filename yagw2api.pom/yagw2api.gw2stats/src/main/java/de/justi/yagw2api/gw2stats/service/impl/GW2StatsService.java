package de.justi.yagw2api.gw2stats.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

import de.justi.yagw2api.gw2stats.dto.IAPIStatusDTO;
import de.justi.yagw2api.gw2stats.dto.IGW2StatsDTOFactory;
import de.justi.yagw2api.gw2stats.service.IGW2StatsService;
import de.justi.yagwapi.common.AbstractService;
import de.justi.yagwapi.common.utils.RetryClientFilter;

final class GW2StatsService extends AbstractService implements IGW2StatsService {
	private static final int RETRY_COUNT = 10;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(GW2StatsService.class);
	private static final URL STATUS_URL;
	private static final URL STATUS_CODES_URL;
	static {
		try {
			STATUS_URL = new URL("http://gw2stats.net/api/status.json");
			STATUS_CODES_URL = new URL("http://gw2stats.net/api/status_codes.json");
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

	public IAPIStatusDTO retrieveAPIStatus() {
		final WebResource resource = CLIENT.resource(STATUS_URL.toExternalForm());
		resource.addFilter(new RetryClientFilter(RETRY_COUNT));
		final WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON_TYPE);
		try {
			final String response = builder.get(String.class).replaceAll(Pattern.quote("\\"), "");
			LOGGER.trace("Retrieved response=" + response);
			final IAPIStatusDTO result = GW2StatsService.this.gw2statsDTOFactory.newAPIDTOOf(response);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Built result=" + result);
			}
			return result;
		} catch (ClientHandlerException e) {
			LOGGER.fatal("Exception thrown while quering " + resource.getURI(), e);
			return null;
		}
	}
}
