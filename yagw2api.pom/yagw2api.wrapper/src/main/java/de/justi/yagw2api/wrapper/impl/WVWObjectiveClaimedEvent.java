package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IGuild;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;

final class WVWObjectiveClaimedEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveClaimedEvent {

	private final IGuild claimingGuild;
	private final Optional<IGuild> previousClaimedByGuild;
	
	public WVWObjectiveClaimedEvent(IWVWObjective source, IGuild claimingGuild, IGuild previousClaimedByGuild) {
		super(source);
		this.claimingGuild = checkNotNull(claimingGuild);
		this.previousClaimedByGuild = Optional.fromNullable(previousClaimedByGuild);
	}

	@Override
	public IGuild getClaimingGuild() {
		return this.claimingGuild;
	}

	@Override
	public Optional<IGuild> previousClaimedByGuild() {
		return this.previousClaimedByGuild;
	}


}
