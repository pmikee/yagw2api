package de.justi.yagw2api.wrapper.domain;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.domain.guild.DefaultGuild;
import de.justi.yagw2api.wrapper.domain.guild.Guild;
import de.justi.yagw2api.wrapper.domain.world.World;
import de.justi.yagw2api.wrapper.domain.world.World.WorldBuilder;
import de.justi.yagw2api.wrapper.domain.world.DefaultWorld;

public final class DefaultModelFactory implements ModelFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultModelFactory.class);
	private final Map<String, Guild> guildsMappedById = new HashMap<String, Guild>();
	private final Map<Integer, World> worldsMappedById = new HashMap<Integer, World>();

	@Override
	public Guild getOrCreateGuild(final String id, final String name, final String tag) {
		checkNotNull(id);
		if (!this.guildsMappedById.containsKey(id)) {
			synchronized (this) {
				if (!this.guildsMappedById.containsKey(id)) {
					this.guildsMappedById.put(id, new DefaultGuild(id, name, tag));
				}
			}
		}
		checkState(this.guildsMappedById.containsKey(id));
		return this.guildsMappedById.get(id);
	}

	@Override
	public Optional<World> getWorld(final int id) {
		checkArgument(id > 0);
		return Optional.fromNullable(this.worldsMappedById.get(id));
	}

	@Override
	public WorldBuilder newWorldBuilder() {
		return new DefaultWorld.DefaultWorldBuilder();
	}

	@Override
	public void clearCache() {
		LOGGER.info("Going to clear cache of {}", this);
		this.guildsMappedById.clear();
		this.worldsMappedById.clear();
	}

}
