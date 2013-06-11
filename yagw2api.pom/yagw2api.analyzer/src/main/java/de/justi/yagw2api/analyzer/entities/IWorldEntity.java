package de.justi.yagw2api.analyzer.entities;

import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IWorld;

public interface IWorldEntity extends IEntity {
	Optional<String> getName(Locale locale);

	Optional<String> getName();

	Optional<String> getNameDE();

	Optional<String> getNameEN();

	Optional<String> getNameES();

	Optional<String> getNameFR();

	Optional<Integer> getOriginWorldId();

	/**
	 * <p>
	 * Synchronize this with the given {@link IWorld}
	 * </p>
	 * <p>
	 * Synchronization is not persisted directly, therefore you have to use
	 * {@link IWorldEnityDAO#save(IWorldEntity)} to persist them.
	 * </p>
	 * 
	 * @param model
	 * @return true if successfully, else false
	 */
	boolean synchronizeWithModel(IWorld model);
}
