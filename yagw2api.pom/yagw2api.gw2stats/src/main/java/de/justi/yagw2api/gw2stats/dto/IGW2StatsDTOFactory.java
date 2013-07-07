package de.justi.yagw2api.gw2stats.dto;

import java.util.Map;

public interface IGW2StatsDTOFactory {
	Map<String, IAPIStateDTO> newAPIStatesOf(String json);

	Map<String, IAPIStateDescriptionDTO> newAPIStateDescriptionsOf(String json);
}
