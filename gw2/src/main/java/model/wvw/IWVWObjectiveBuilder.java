package model.wvw;

import api.dto.IWVWObjectiveDTO;

public interface IWVWObjectiveBuilder {
	IWVWObjective build();
	IWVWObjectiveBuilder location(IWVWLocationType location);
	IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto);
}
