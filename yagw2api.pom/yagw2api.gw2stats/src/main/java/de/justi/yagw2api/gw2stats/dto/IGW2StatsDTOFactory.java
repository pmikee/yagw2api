package de.justi.yagw2api.gw2stats.dto;

public interface IGW2StatsDTOFactory {
	IAPIStatesDTO newAPIStatesOf(String json);

	IAPIStateDescriptionsDTO newAPIStateDescriptionsOf(String json);
}
