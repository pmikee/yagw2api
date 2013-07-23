package de.justi.yagw2api.wrapper.impl;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IGuild;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;

final class WVWObjectiveUnclaimedEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveUnclaimedEvent {
	private final Optional<IGuild> previousClaimedByGuild;
	
	public WVWObjectiveUnclaimedEvent(IWVWObjective source, IGuild previousClaimedByGuild) {
		super(source);
		this.previousClaimedByGuild = Optional.fromNullable(previousClaimedByGuild);
	}
	@Override
	public Optional<IGuild> previousClaimedByGuild() {
		return this.previousClaimedByGuild;
	}


}
