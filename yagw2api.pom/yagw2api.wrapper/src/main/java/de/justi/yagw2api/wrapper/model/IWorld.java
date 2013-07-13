package de.justi.yagw2api.wrapper.model;

import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.IWorldNameDTO;
import de.justi.yagw2api.wrapper.model.types.IWorldLocationType;

public interface IWorld {

	static interface IWorldBuilder {
		IWorld build();

		IWorldBuilder fromDTO(IWorldNameDTO dto);

		IWorldBuilder worldLocation(IWorldLocationType location);

		IWorldBuilder name(String name);

		IWorldBuilder id(int id);

		IWorldBuilder worldLocale(Locale locale);
	}

	int getId();

	Optional<String> getName();

	void setName(String name);

	Optional<Locale> getWorldLocale();

	IWorldLocationType getWorldLocation();

	IWorld createUnmodifiableReference();
}
