package de.justi.yagw2api.gw2stats.service;

import java.util.Map;

import com.google.common.base.Optional;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionDTO;

public interface IGW2StatsService {
	Map<String, IAPIStateDTO> retrieveAPIStates();

	Map<String, IAPIStateDescriptionDTO> retrieveAPIStateDescriptions();

	Optional<IAPIStateDescriptionDTO> retrieveAPIStateDescription(String state);
}
