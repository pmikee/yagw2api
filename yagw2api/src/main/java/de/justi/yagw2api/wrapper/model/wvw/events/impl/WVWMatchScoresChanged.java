package de.justi.yagw2api.wrapper.model.wvw.events.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.IWVWScores;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;

class WVWMatchScoresChanged extends AbstractWVWScoresChangedEvent implements IWVWMatchScoresChangedEvent {
	private final IWVWMatch match;

	public WVWMatchScoresChanged(IWVWScores scores,int deltaRed, int deltaGreen, int deltaBlue, IWVWMatch match) {
		super(checkNotNull(scores),deltaRed, deltaGreen, deltaBlue);
		this.match = checkNotNull(match);
	}

	@Override
	public IWVWMatch getMatch() {
		return this.match;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("super", super.toString()).add("scores", this.getScores()).add("delta", "r:"+this.getDeltaRed()+",g:"+this.getDeltaGreen()+",b:"+this.getDeltaBlue()).add("matchId", this.getMatch().getId()).toString();
	}
}
