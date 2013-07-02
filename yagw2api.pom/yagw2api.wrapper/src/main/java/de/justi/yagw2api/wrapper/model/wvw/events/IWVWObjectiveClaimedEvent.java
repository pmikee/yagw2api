package de.justi.yagw2api.wrapper.model.wvw.events;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.model.IGuild;

public interface IWVWObjectiveClaimedEvent extends IWVWObjectiveEvent {
	IGuild getClaimingGuild();
	Optional<IGuild> previousClaimedByGuild();
}
