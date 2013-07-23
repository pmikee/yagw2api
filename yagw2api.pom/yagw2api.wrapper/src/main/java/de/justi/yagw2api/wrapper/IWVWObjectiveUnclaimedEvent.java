package de.justi.yagw2api.wrapper;

import com.google.common.base.Optional;

public interface IWVWObjectiveUnclaimedEvent extends IWVWObjectiveEvent {
	Optional<IGuild> previousClaimedByGuild();

}
