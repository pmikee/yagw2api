package de.justi.yagw2api.wrapper;

import com.google.common.base.Optional;

public interface IWVWObjectiveClaimedEvent extends IWVWObjectiveEvent {
	IGuild getClaimingGuild();
	Optional<IGuild> previousClaimedByGuild();
}
