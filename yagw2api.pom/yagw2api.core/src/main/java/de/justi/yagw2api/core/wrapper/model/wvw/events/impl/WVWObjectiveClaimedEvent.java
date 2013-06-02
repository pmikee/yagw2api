package de.justi.yagw2api.core.wrapper.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;

public class WVWObjectiveClaimedEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveClaimedEvent {

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
