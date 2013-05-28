package de.justi.yagw2api.wrapper.model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import de.justi.yagw2api.utils.InjectionHelper;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWModelEventFactory;

public class WVWMatchScores extends AbstractWVWScores {
	private static final Logger LOGGER = Logger.getLogger(WVWMatchScores.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelEventFactory.class);

	private final IWVWMatch match;

	public WVWMatchScores(IWVWMatch match) {
		this.match = checkNotNull(match);
	}

	@Override
	protected void onChange(int deltaRed, int deltaGreen, int deltaBlue) {
		final IWVWMatchScoresChangedEvent event = WVW_MODEL_EVENT_FACTORY.newMatchScoresChangedEvent(this.createImmutableReference(),deltaRed, deltaGreen, deltaBlue,
				this.match.createImmutableReference());
		checkState(event != null);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to post new " + event);
		}
		this.getChannel().post(event);
	}

}
