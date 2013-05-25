package model.wvw;

import java.util.Locale;

import api.dto.IWVWMatchDTO;

public interface IWVWMatchBuilder {
	IWVWMatch build();
	IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale);
}
