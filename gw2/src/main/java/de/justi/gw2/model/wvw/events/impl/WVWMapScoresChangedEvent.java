package de.justi.gw2.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Objects.ToStringHelper;

import de.justi.gw2.model.wvw.IWVWMap;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWScores;
import de.justi.gw2.model.wvw.events.IWVWMapScoresChangedEvent;

class WVWMapScoresChangedEvent extends AbstractWVWScoresChangedEvent implements IWVWMapScoresChangedEvent {
	private final IWVWMap map;
	
	public WVWMapScoresChangedEvent(IWVWScores scores,int deltaRed, int deltaGreen, int deltaBlue, IWVWMap map) {
		super(checkNotNull(scores), deltaRed, deltaGreen, deltaBlue);
		this.map = checkNotNull(map);
	}

	@Override
	public IWVWMap getMap() {
		return this.map;
	}

	public String toString() {
		final Optional<IWVWMatch> matchOptional = this.getMap().getMatch();
		final ToStringHelper helper = Objects.toStringHelper(this).add("super", super.toString()).add("scores", this.getScores()).add("delta", "r:"+this.getDeltaRed()+",g:"+this.getDeltaGreen()+",b:"+this.getDeltaBlue()).add("mapType",this.getMap().getType());
		if(matchOptional.isPresent()) {
			helper.add("matchId", matchOptional.get().getId());
		}
		return helper.toString();
	}
}
