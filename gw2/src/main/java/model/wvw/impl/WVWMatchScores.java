package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.wvw.IWVWMatch;
import model.wvw.events.IWVWModelEventFactory;
import utils.InjectionHelper;

public class WVWMatchScores extends AbstractWVWScores {
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelEventFactory.class);
	
	private final IWVWMatch match;
	
	public WVWMatchScores(IWVWMatch match) {
		this.match = checkNotNull(match);
	}
	
	@Override
	protected void onChange() {
		this.getChannel().post(WVW_MODEL_EVENT_FACTORY.newMatchScoresChangedEvent(this.createImmutableReference(), this.match.createImmutableReference()));
	}

}
