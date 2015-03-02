package de.justi.yagw2api.locationprovider;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.locationprovider
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import yagw2api.server.character.Character;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import de.justi.yagw2api.mumblelink.IMumbleLink;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatar;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkMapChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;
import de.justi.yagw2api.mumblelink.YAGW2APIMumbleLink;
import de.justi.yagw2api.mumblelink.impl.IMumbleLinkListener;

public final class LocationProviderApplication implements IMumbleLinkListener, Runnable {
	public static final Logger LOGGER = LoggerFactory.getLogger(LocationProviderApplication.class);

	public static void main(final String[] args) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Going to start {} using arg={}", LocationProviderApplication.class, Arrays.deepToString(args));
		}

		final String host;
		if (args.length == 1) {
			host = args[0];
		} else {
			host = System.getProperty("yagw2api.host", "localhost");
		}

		final LocationProviderApplication app = new LocationProviderApplication(host, YAGW2APIMumbleLink.INSTANCE.getMumbleLink());
		app.run();
	}

	// FIELDS
	private final String host;
	private final IMumbleLink link;
	private Optional<Character> currentCharacter = Optional.absent();
	private final RestTemplate restTemplate = new RestTemplate();

	// CONSTRUCTOR
	private LocationProviderApplication(final String host, final IMumbleLink link) {
		this.host = checkNotNull(host, "missing host");
		this.link = checkNotNull(link, "missing link");
		this.link.registerMumbleLinkListener(this);
	}

	// METHODS

	@Override
	public void run() {
		this.link.setActive(true);
		while (this.link.isActive()) {
			try {
				LOGGER.info("{} is still running", this);
				Thread.sleep(TimeUnit.SECONDS.toMillis(60));
			} catch (InterruptedException e) {
				this.link.setActive(false);
			}
		}
	}

	private final void updateCharacterFromAvatar() {
		try {
			if (this.link.getAvatar().isPresent() && this.link.getAvatarPosition().isPresent()) {
				final IMumbleLinkAvatar avatar = this.link.getAvatar().get();
				final IMumbleLinkPosition avatarPosition = this.link.getAvatarPosition().get();
				final Map<String, ?> parameters = ImmutableMap.<String, Object> builder().put("host", this.host).put("name", avatar.getName())
						.put("commander", avatar.isCommander()).put("worldId", avatar.getWorldId()).put("mapId", avatar.getMapId()).put("x", avatarPosition.getX())
						.put("y", avatarPosition.getY()).put("z", avatarPosition.getZ()).build();
				this.currentCharacter = Optional
						.fromNullable(this.restTemplate.postForEntity(
								"http://{host}:8080/character/{name}?x={x}&y={y}&z={z}&worldId={worldId}&mapId={mapId}&commander={commander}", null, Character.class, parameters)
								.getBody());
				LOGGER.debug("Updated character: {}", this.currentCharacter);
			}
		} catch (Throwable t) {
			LOGGER.error("unexpected exception while updating character", t);
			throw t;
		}
	}

	@Override
	public void onAvatarChange(final IMumbleLinkAvatarChangeEvent event) {
		this.updateCharacterFromAvatar();
	}

	@Override
	public void onMapChange(final IMumbleLinkMapChangeEvent event) {
		this.updateCharacterFromAvatar();
	}

	@Override
	public void onAvatarPositionChange(final IMumbleLinkAvatarPositionChangeEvent event) {
		this.updateCharacterFromAvatar();
	}

	@Override
	public void onAvatarFrontChange(final IMumbleLinkAvatarFrontChangeEvent event) {
		// NOP
	}

	@Override
	public void onAvatarTopChange(final IMumbleLinkAvatarTopChangeEvent event) {
		// NOP
	}

	@Override
	public void onCameraPositionChange(final IMumbleLinkCameraPositionChangeEvent event) {
		// NOP
	}

	@Override
	public void onCameraFrontChange(final IMumbleLinkCameraFrontChangeEvent event) {
		// NOP
	}

	@Override
	public void onCameraTopChange(final IMumbleLinkCameraTopChangeEvent event) {
		// NOP
	}

}
