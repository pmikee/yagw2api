package de.justi.yagw2api.core.wrapper.model.wvw.events.impl;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveUnclaimedEvent;

public class WVWObjectiveUnclaimedEvent extends AbstractWVWObjectiveEvent implements IWVWObjectiveUnclaimedEvent {
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
