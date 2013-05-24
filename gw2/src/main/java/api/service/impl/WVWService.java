package api.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.ws.rs.core.MediaType;

import api.service.IWVWService;
import api.service.dto.IWVWDTOFactory;
import api.service.dto.IWVWMatchDetailsDTO;
import api.service.dto.IWVWMatchesDTO;
import api.service.dto.IWVWObjectiveNameDTO;
import api.service.dto.IWorldNameDTO;

import com.google.inject.Inject;
import com.sun.jersey.api.client.WebResource;

public class WVWService extends AbstractService implements IWVWService{
	private static final String API_VERSION ="v1";
	
	private URL matchesBaseURL;
	private URL matchDetailsBaseURL;
	private URL objectiveNamesBaseURL;
	private URL worldNamesBaseURL;
	
	@Inject
	private IWVWDTOFactory wvwDTOFactory;
	
	public WVWService(){
		try {
			this.matchesBaseURL = new URL("https://api.guildwars2.com/"+API_VERSION+"/wvw/matches.json");
			this.matchDetailsBaseURL = new URL("https://api.guildwars2.com/"+API_VERSION+"/wvw/match_details.json");
			this.objectiveNamesBaseURL = new URL("https://api.guildwars2.com/"+API_VERSION+"/wvw/objective_names.json");
			this.worldNamesBaseURL = new URL("https://api.guildwars2.com/"+API_VERSION+"/world_names.json");
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Failed to initialize URLs.", e);
		}
	}
	
	public IWVWMatchesDTO retrieveAllMatches() {
		final WebResource resource = CLIENT.resource(this.matchesBaseURL.toExternalForm());
		final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		return this.wvwDTOFactory.createMatchesDTOfromJSON(response);
	}

	public IWVWMatchDetailsDTO retrieveMatchDetails(String id) {
		checkNotNull(id);
		final WebResource resource = CLIENT.resource(this.matchDetailsBaseURL.toExternalForm()).queryParam("match_id", id);
		final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		return this.wvwDTOFactory.createMatchDetailsfromJSON(response);
	}

	public IWVWObjectiveNameDTO[] retrieveAllObjectiveNames(Locale locale) {
		checkNotNull(locale);
		final WebResource resource = CLIENT.resource(this.objectiveNamesBaseURL.toExternalForm()).queryParam("lang", locale.getLanguage());
		final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		return this.wvwDTOFactory.createObjectiveNamesFromJSON(response);
	}

	public IWorldNameDTO[] retrieveAllWorldNames(Locale locale) {
		checkNotNull(locale);
		final WebResource resource = CLIENT.resource(this.worldNamesBaseURL.toExternalForm()).queryParam("lang", locale.getLanguage());
		final String response = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		return this.wvwDTOFactory.createWorldNamesFromJSON(response);
	}

}
