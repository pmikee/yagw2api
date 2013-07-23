package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;

final class WVWMatchScores extends AbstractWVWScores {
	private static final Logger LOGGER = Logger.getLogger(WVWMatchScores.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelEventFactory();

	private final IWVWMatch match;

	public WVWMatchScores(IWVWMatch match) {
		this.match = checkNotNull(match);
	}

	@Override
	protected void onChange(int deltaRed, int deltaGreen, int deltaBlue) {
		final IWVWMatchScoresChangedEvent event = WVW_MODEL_EVENT_FACTORY.newMatchScoresChangedEvent(this.createUnmodifiableReference(), deltaRed, deltaGreen, deltaBlue,
				this.match.createUnmodifiableReference());
		checkState(event != null);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to post new " + event);
		}
		this.getChannel().post(event);
	}

}
