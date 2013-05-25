package model.wvw;

import model.IWorld;
import api.dto.IWVWObjectiveDTO;

public interface IWVWObjectiveBuilder {
	IWVWObjective build();
	IWVWObjectiveBuilder location(IWVWLocationType location);
	IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto);
	IWVWObjectiveBuilder owner(IWorld world);
}
