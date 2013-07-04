package de.justi.yagw2api.gw2stats.dto;

import com.google.common.base.Optional;

public interface IAPIStateDTO {

	String getTime();

	int getRecords();

	int getRetrieve();

	int getPing();

	String getStatus();

	Optional<IAPIStateDescriptionDTO> getDescription();

}
