package de.justi.yagw2api.locationprovider;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import yagw2api.server.character.Avatar;

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
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_PORT = "8080";
	private static final String HOST_SYSTEM_PROPERTY = "yagw2api.host";
	private static final String PORT_SYSTEM_PROPERTY = "yagw2api.port";

	private static final String REST_URI_PARAMETER_HOST = "host";
	private static final String REST_URI_PARAMETER_PORT = "port";
	private static final String REST_URI_PARAMETER_NAME = "name";
	private static final String REST_URI_PARAMETER_XPOSITION = "x";
	private static final String REST_URI_PARAMETER_YPOSITION = "y";
	private static final String REST_URI_PARAMETER_ZPOSITION = "z";
	private static final String REST_URI_PARAMETER_WORLDID = "worldId";
	private static final String REST_URI_PARAMETER_MAPID = "mapId";
	private static final String REST_URI_PARAMETER_COMMANDER = "commander";
	private static final String REST_URI_TEMPLATE = "http://{" + REST_URI_PARAMETER_HOST + "}:{" + REST_URI_PARAMETER_PORT + "}/character/{" + REST_URI_PARAMETER_NAME + "}?x={"
			+ REST_URI_PARAMETER_XPOSITION + "}&y={" + REST_URI_PARAMETER_YPOSITION + "}&z={" + REST_URI_PARAMETER_ZPOSITION + "}&worldId={" + REST_URI_PARAMETER_WORLDID
			+ "}&mapId={" + REST_URI_PARAMETER_MAPID + "}&commander={" + REST_URI_PARAMETER_COMMANDER + "}";

	public static final Logger LOGGER = LoggerFactory.getLogger(LocationProviderApplication.class);

	public static void main(final String[] args) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Going to start {} using arg={}", LocationProviderApplication.class, Arrays.deepToString(args));
		}

		final String host;
		final int port;
		if (args.length >= 1) {
			host = args[0];
		} else {
			host = System.getProperty(HOST_SYSTEM_PROPERTY, DEFAULT_HOST);
		}
		if (args.length >= 2) {
			port = Integer.valueOf(args[1]);
		} else {
			port = Integer.valueOf(System.getProperty(PORT_SYSTEM_PROPERTY, DEFAULT_PORT));
		}

		final LocationProviderApplication app = new LocationProviderApplication(host, port, YAGW2APIMumbleLink.INSTANCE.getMumbleLink());
		app.run();
	}

	// FIELDS
	private final String host;
	private final int port;
	private final IMumbleLink link;
	private Optional<Avatar> currentCharacter = Optional.absent();
	private final RestTemplate restTemplate = new RestTemplate();

	// CONSTRUCTOR
	private LocationProviderApplication(final String host, final int port, final IMumbleLink link) {
		this.host = checkNotNull(host, "missing host");
		this.port = port;
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
				//@formatter:off
				final Map<String, ?> parameters = ImmutableMap.<String, Object> builder().
						put(REST_URI_PARAMETER_HOST, this.host).
						put(REST_URI_PARAMETER_PORT, this.port).
						put(REST_URI_PARAMETER_NAME, avatar.getName()).
						put(REST_URI_PARAMETER_COMMANDER, avatar.isCommander()).
						put(REST_URI_PARAMETER_WORLDID, avatar.getWorldId()).
						put(REST_URI_PARAMETER_MAPID, avatar.getMapId()).
						put(REST_URI_PARAMETER_XPOSITION, avatarPosition.getX()).
						put(REST_URI_PARAMETER_YPOSITION, avatarPosition.getY()).
						put(REST_URI_PARAMETER_ZPOSITION, avatarPosition.getZ()).
						build();
				//@formatter:on
				this.currentCharacter = Optional.fromNullable(this.restTemplate.postForEntity(REST_URI_TEMPLATE, null, Avatar.class, parameters).getBody());
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
