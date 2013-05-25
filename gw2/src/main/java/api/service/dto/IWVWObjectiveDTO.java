package api.service.dto;

import java.util.Locale;

import com.google.common.base.Optional;

public interface IWVWObjectiveDTO {
	int getId();
	Optional<IWVWObjectiveNameDTO> getName(Locale locale); 
	String getOwner();
	String getGuildId();
}
