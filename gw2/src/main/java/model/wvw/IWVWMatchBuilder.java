package model.wvw;

import java.util.Locale;

import api.dto.IWVWMatchDTO;

public interface IWVWMatchBuilder {
	IWVWMatch build();
	IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale);
	IWVWMatchBuilder redScore(int score);
	IWVWMatchBuilder blueScore(int score);
	IWVWMatchBuilder greenScore(int score);
}
