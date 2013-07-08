package de.justi.yagw2api.gw2stats.dto;

import java.util.Date;

import com.google.common.base.Optional;

public interface IAPIStateDTO {

	Date getTime();

	int getRecords();

	int getRetrieve();

	int getPing();

	String getStatus();

	Optional<IAPIStateDescriptionDTO> getDescription();

}
