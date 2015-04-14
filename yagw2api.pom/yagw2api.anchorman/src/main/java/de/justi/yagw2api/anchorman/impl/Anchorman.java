package de.justi.yagw2api.anchorman.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Anchorman
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import de.justi.yagw2api.anchorman.IAnchorman;
import de.justi.yagw2api.arenanet.Arenanet;
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
import de.justi.yagw2api.wrapper.domain.wvw.WVWMapType;
import de.justi.yagw2api.wrapper.domain.wvw.WVWMatch;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWMapListener;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWMatchListener;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;
import de.justi.yagwapi.common.TTSUtils;

class Anchorman implements IAnchorman, IMumbleLinkListener, WVWMatchListener, WVWMapListener {
	private static final String BUNDLE_KEY_COMPLETED_MATCH_INITIALIZATION = "completed_match_initialization";
	private static final String BUNDLE_KEY_OBJECTIVE_UNCLAIMED = "objective_unclaimed";
	private static final String BUNDLE_KEY_OBJECTIVE_CLAIMED = "objective_claimed";
	private static final String BUNDLE_KEY_OBJECTIVE_OUT_OF_BUFF = "objective_out_of_buff";
	private static final String BUNDLE_KEY_OBJECTIVE_CAPTURED = "objective_captured";
	private static final String BUNDLE_KEY_ENTERED_MAP = "entered_map";
	private static final String BUNDLE_KEY_LEFT_MAP = "left_map";
	private static final String BUNDLE_KEY_CHANGED_MAP = "changed_map";
	private static final String BUNDLE_KEY_LOGGED_IN = "logged_in";
	private static final String BUNDLE_KEY_LOGGED_OUT = "logged_out";
	private static final String BUNDLE_KEY_CHANGED_CHARACTER = "changed_character";
	private static final String BUNDLE_BASENAME = "anchorman";
	private static final Arenanet ARENANET = YAGW2APIArenanet.INSTANCE;
	private static final boolean BUNDLE_KEY_COMPLETED_MATCH_INITIALIZATION_TOGGLE = false;

	// FIXME make this atomicbooleans
	private volatile boolean running = false;
	private volatile boolean initialized = false;

	private Optional<IMumbleLink> mumbleLink = Optional.absent();
	private Optional<WVWWrapper> wrapper = Optional.absent();

	private Optional<Set<WVWMatch>> matchFilter = Optional.of(Collections.<WVWMatch> emptySet());
	private Optional<Set<WVWMapType>> mapTypeFilter = Optional.absent();

	@Override
	public void init(final WVWWrapper wrapper, final IMumbleLink mumbleLink) {
		checkNotNull(wrapper);
		checkNotNull(mumbleLink);
		synchronized (this) {
			checkState(!this.initialized, "%s is already initialized.", this);
			this.mumbleLink = Optional.of(mumbleLink);
			this.mumbleLink.get().registerMumbleLinkListener(this);
			this.wrapper = Optional.of(wrapper);
			this.wrapper.get().registerWVWMatchListener(this);
			this.wrapper.get().registerWVWMapListener(this);
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

	private void readOut(final String textKey, final int priority, final Object... arguments) {
		final Locale locale = ARENANET.getCurrentLocale();
		final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASENAME, locale);
		TTSUtils.readOut(bundle.getString(textKey), locale, priority, arguments);
	}

	@Override
	public void onAvatarChange(final IMumbleLinkAvatarChangeEvent event) {
		checkNotNull(event);
		if (this.isRunning()) {
			if (event.getOldAvatar().isPresent() && event.getNewAvatar().isPresent()) {
				this.readOut(BUNDLE_KEY_CHANGED_CHARACTER, Integer.MAX_VALUE, event.getOldAvatar().get().getName(), event.getNewAvatar().get().getName());
			} else if (event.getOldAvatar().isPresent()) {
				checkState(!event.getNewAvatar().isPresent());
				this.readOut(BUNDLE_KEY_LOGGED_OUT, Integer.MAX_VALUE, event.getOldAvatar().get().getName());
			} else if (event.getNewAvatar().isPresent()) {
				checkState(!event.getOldAvatar().isPresent());
				this.readOut(BUNDLE_KEY_LOGGED_IN, Integer.MAX_VALUE, event.getNewAvatar().get().getName());
			} else {
				throw new IllegalStateException(this + " is unable to handle " + event);
			}
		}
	}

	@Override
	public void onMapChange(final IMumbleLinkMapChangeEvent event) {
		checkNotNull(event);
		if (this.isRunning()) {
			if (event.getOldMapId().isPresent() && event.getNewMapId().isPresent()) {
				this.readOut(BUNDLE_KEY_CHANGED_MAP, Integer.MAX_VALUE, event.getOldMapId().get(), event.getNewMapId().get());
			} else if (event.getOldMapId().isPresent()) {
				checkState(!event.getNewMapId().isPresent());
				this.readOut(BUNDLE_KEY_LEFT_MAP, Integer.MAX_VALUE, event.getOldMapId().get());
			} else if (event.getNewMapId().isPresent()) {
				checkState(!event.getOldMapId().isPresent());
				this.readOut(BUNDLE_KEY_ENTERED_MAP, Integer.MAX_VALUE, event.getNewMapId().get());
			} else {
				throw new IllegalStateException(this + " is unable to handle " + event);
			}
		}
	}

	@Override
	public void onAvatarPositionChange(final IMumbleLinkAvatarPositionChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onAvatarFrontChange(final IMumbleLinkAvatarFrontChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onAvatarTopChange(final IMumbleLinkAvatarTopChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onCameraPositionChange(final IMumbleLinkCameraPositionChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onCameraFrontChange(final IMumbleLinkCameraFrontChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onCameraTopChange(final IMumbleLinkCameraTopChangeEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onMatchScoreChangedEvent(final WVWMatchScoresChangedEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onInitializedMatchForWrapper(final WVWInitializedMatchEvent event) {
		checkNotNull(event);
		if (BUNDLE_KEY_COMPLETED_MATCH_INITIALIZATION_TOGGLE) {
			if (this.isRunning()) {
				this.readOut(BUNDLE_KEY_COMPLETED_MATCH_INITIALIZATION, Integer.MAX_VALUE - 3, event.getMatch().getGreenWorld().getName().get(), event.getMatch().getBlueWorld()
						.getName().get(), event.getMatch().getRedWorld().getName().get());
			}
		}
	}

	@Override
	public void onChangedMapScoreEvent(final WVWMapScoresChangedEvent event) {
		// NOTHING TO DO
	}

	@Override
	public void onObjectiveCapturedEvent(final WVWObjectiveCaptureEvent event) {
		checkNotNull(event);
		checkArgument(event.getMap().getMatch().isPresent());
		if (this.isRunning() && this.checkWVWMatchFilter(event.getMap().getMatch().get()) && this.checkWVWMapTypeFilter(event.getMap().getType())) {
			this.readOut(BUNDLE_KEY_OBJECTIVE_CAPTURED, Integer.MAX_VALUE, event.getObjective().getLabel().get(), event.getMap().getType().getLabel(ARENANET.getCurrentLocale())
					.get(), event.getNewOwningWorld().getName().get());
		}
	}

	@Override
	public void onObjectiveEndOfBuffEvent(final WVWObjectiveEndOfBuffEvent event) {
		checkNotNull(event);
		checkArgument(event.getMap().getMatch().isPresent());
		if (this.isRunning() && this.checkWVWMatchFilter(event.getMap().getMatch().get()) && this.checkWVWMapTypeFilter(event.getMap().getType())) {
			this.readOut(BUNDLE_KEY_OBJECTIVE_OUT_OF_BUFF, Integer.MAX_VALUE - 1, event.getObjective().getLabel().get(),
					event.getMap().getType().getLabel(ARENANET.getCurrentLocale()).get());
		}
	}

	@Override
	public void onObjectiveClaimedEvent(final WVWObjectiveClaimedEvent event) {
		checkNotNull(event);
		checkArgument(event.getMap().getMatch().isPresent());
		if (this.isRunning() && this.checkWVWMatchFilter(event.getMap().getMatch().get()) && this.checkWVWMapTypeFilter(event.getMap().getType())) {
			this.readOut(BUNDLE_KEY_OBJECTIVE_CLAIMED, -1, event.getObjective().getLabel().get(), event.getMap().getType().getLabel(ARENANET.getCurrentLocale()).get(), event
					.getClaimingGuild().getName());
		}
	}

	@Override
	public void onObjectiveUnclaimedEvent(final WVWObjectiveUnclaimedEvent event) {
		checkNotNull(event);
		checkArgument(event.getMap().getMatch().isPresent());
		if (this.isRunning() && this.checkWVWMatchFilter(event.getMap().getMatch().get()) && this.checkWVWMapTypeFilter(event.getMap().getType())) {
			this.readOut(BUNDLE_KEY_OBJECTIVE_UNCLAIMED, -1, event.getObjective().getLabel().get(), event.getMap().getType().getLabel(ARENANET.getCurrentLocale()).get(), event
					.previousClaimedByGuild().get().getName());
		}
	}

	private boolean checkWVWMatchFilter(final WVWMatch match) {
		checkNotNull(match);
		return !this.matchFilter.isPresent() || this.matchFilter.get().contains(match);
	}

	private boolean checkWVWMapTypeFilter(final WVWMapType type) {
		checkNotNull(type);
		return !this.mapTypeFilter.isPresent() || this.mapTypeFilter.get().contains(type);
	}

	@Override
	public void setWVWMatchFilter(final WVWMatch... matches) {
		checkNotNull(matches);
		this.matchFilter = Optional.<Set<WVWMatch>> of(ImmutableSet.copyOf(matches));
	}

	@Override
	public void setWVWMapTypeFilter(final WVWMapType... mapTypes) {
		checkNotNull(mapTypes);
		this.mapTypeFilter = Optional.<Set<WVWMapType>> of(ImmutableSet.copyOf(mapTypes));
	}

}
