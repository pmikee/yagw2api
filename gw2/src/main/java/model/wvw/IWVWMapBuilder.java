package model.wvw;

import api.dto.IWVWMapDTO;

public interface IWVWMapBuilder {
	IWVWMap build();
	IWVWMapBuilder type(IWVWMapType type);
	IWVWMapBuilder objective(IWVWObjective objective);
	IWVWMapBuilder fromDTO(IWVWMapDTO dto);
}
