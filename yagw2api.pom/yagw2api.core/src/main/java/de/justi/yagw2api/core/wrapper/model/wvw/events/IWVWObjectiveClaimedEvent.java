package de.justi.yagw2api.core.wrapper.model.wvw.events;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IGuild;

public interface IWVWObjectiveClaimedEvent extends IWVWObjectiveEvent {
	IGuild getClaimingGuild();
	Optional<IGuild> previousClaimedByGuild();
}
