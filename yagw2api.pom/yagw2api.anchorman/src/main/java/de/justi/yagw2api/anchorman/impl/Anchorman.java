package de.justi.yagw2api.anchorman.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import de.justi.yagw2api.anchorman.IAnchorman;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.mumblelink.IMumbleLink;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkMapChangeEvent;
import de.justi.yagw2api.mumblelink.impl.IMumbleLinkListener;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagwapi.common.TTSUtils;

class Anchorman implements IAnchorman, IMumbleLinkListener, IWVWMatchListener, IWVWMapListener {

	private volatile boolean running = false;
	private volatile boolean initialized = false;

	@Override
	public void init(IWVWWrapper wrapper, IMumbleLink mumblelink) {
		checkNotNull(wrapper);
		checkNotNull(mumblelink);
		synchronized (this) {
			checkState(!this.initialized, "%s is already initialized.", this);
			// wrapper.registerWVWMatchListener(this);
			// wrapper.registerWVWMapListener(this);
			mumblelink.registerMumbleLinkListener(this);
		}
	}

	@Override
	public void start() {
		this.running = true;
	}

	@Override
	public void stop() {
		this.running = false;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	private void readOut(String text) {
		TTSUtils.readOut(text, YAGW2APIArenanet.INSTANCE.getCurrentLocale());
	}

	@Override
	public void onAvatarChange(IMumbleLinkAvatarChangeEvent event) {
		if (this.isRunning()) {
			if (event.getOldAvatarName().isPresent() && event.getNewAvatarName().isPresent()) {
				this.readOut("Sie haben den Charakter von " + event.getOldAvatarName().get() + " auf " + event.getNewAvatarName().get() + " gewechselt.");
			} else if (event.getOldAvatarName().isPresent()) {
				checkState(!event.getNewAvatarName().isPresent());
				this.readOut("Sie haben bisher " + event.getOldAvatarName().get() + " gespielt, sich jetzt aber ausgeloggt.");
			} else if (event.getNewAvatarName().isPresent()) {
				checkState(!event.getOldAvatarName().isPresent());
				this.readOut("Sie sich als " + event.getNewAvatarName().get() + " eingeloggt.");
			} else {
				throw new IllegalStateException(this + " is unable to handle " + event);
			}
		}
	}

	@Override
	public void onMapChange(IMumbleLinkMapChangeEvent event) {
		if (this.isRunning()) {
			if (event.getOldMapId().isPresent() && event.getNewMapId().isPresent()) {
				this.readOut("Sie haben von Karte " + event.getOldMapId().get() + " auf Karte " + event.getNewMapId().get() + " gewechselt.");
			} else if (event.getOldMapId().isPresent()) {
				checkState(!event.getNewMapId().isPresent());
				this.readOut("Sie haben Karte " + event.getOldMapId().get() + " verlassen.");
			} else if (event.getNewMapId().isPresent()) {
				checkState(!event.getOldMapId().isPresent());
				this.readOut("Sie haben Karte " + event.getNewMapId().get() + " betreten.");
			} else {
				throw new IllegalStateException(this + " is unable to handle " + event);
			}
		}
	}

	@Override
	public void onAvatarPositionChange(IMumbleLinkAvatarPositionChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onAvatarFrontChange(IMumbleLinkAvatarFrontChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onAvatarTopChange(IMumbleLinkAvatarTopChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onCameraPositionChange(IMumbleLinkCameraPositionChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onCameraFrontChange(IMumbleLinkCameraFrontChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onCameraTopChange(IMumbleLinkCameraTopChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onInitializedMatchForWrapper(IWVWInitializedMatchEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		TTSUtils.readOut(event.getObjective().getLabel().get() + " wurde von " + event.getNewOwningWorld().getName().get() + " erobert.", YAGW2APIArenanet.getInstance().getCurrentLocale(), 3);
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		TTSUtils.readOut(event.getObjective().getLabel().get() + " hat keinen Buff mehr.", YAGW2APIArenanet.getInstance().getCurrentLocale(), 2);
	}

	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
		TTSUtils.readOut(event.getObjective().getLabel().get() + " wurde von " + event.getClaimingGuild().getName() + " eingenommen.", YAGW2APIArenanet.getInstance().getCurrentLocale(), -1);
	}

	@Override
	public void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event) {
		TTSUtils.readOut(event.getObjective().getLabel().get() + " wurde von " + event.previousClaimedByGuild().get().getName() + " aufgegeben.", YAGW2APIArenanet.getInstance().getCurrentLocale(), -1);
	}

}
