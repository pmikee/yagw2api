package de.justi.yagw2api.gw2stats.service;

import com.google.common.base.Optional;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionsDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStatesDTO;

public interface IGW2StatsService {
	IAPIStatesDTO retrieveAPIStates();

	IAPIStateDescriptionsDTO retrieveAPIStateDescriptions();

	Optional<IAPIStateDescriptionDTO> retrieveAPIStateDescription(String state);
}
