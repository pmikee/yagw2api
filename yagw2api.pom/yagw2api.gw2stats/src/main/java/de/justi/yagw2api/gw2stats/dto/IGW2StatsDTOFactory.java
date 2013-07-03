package de.justi.yagw2api.gw2stats.dto;

public interface IGW2StatsDTOFactory {
	IAPIStatusDTO newAPIDTOOf(String json);
}
