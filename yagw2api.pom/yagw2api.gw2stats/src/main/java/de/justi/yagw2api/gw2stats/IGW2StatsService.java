package de.justi.yagw2api.gw2stats;

import java.util.Map;

import com.google.common.base.Optional;

public interface IGW2StatsService {
	Map<String, IAPIStateDTO> retrieveAPIStates();

	Map<String, IAPIStateDescriptionDTO> retrieveAPIStateDescriptions();

	Optional<IAPIStateDescriptionDTO> retrieveAPIStateDescription(String state);
}
