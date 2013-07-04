package de.justi.yagw2api.gw2stats.dto;

import com.google.common.base.Optional;

public interface IAPIStateDescriptionsDTO {

	Optional<IAPIStateDescriptionDTO> getDescriptionOfState(String state);

	IAPIStateDescriptionDTO getSlowRetrieveDescription();

	IAPIStateDescriptionDTO getHighPingDescription();

	IAPIStateDescriptionDTO getIncreasingPingDescription();

	IAPIStateDescriptionDTO getPartialDescription();

	IAPIStateDescriptionDTO getDownDescription();

	IAPIStateDescriptionDTO getUnreachableDescription();

	IAPIStateDescriptionDTO getOkDescription();

}
