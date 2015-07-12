package de.justi.yagw2api.wrapper.guild;

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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

import de.justi.yagw2api.arenanet.v1.GuildService;
import de.justi.yagw2api.arenanet.v1.dto.guild.GuildDetailsDTO;
import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.guild.domain.GuildDomainFactory;
import de.justi.yagw2api.wrapper.guild.domain.NoSuchGuildException;

public final class DefaultGuildWrapper implements GuildWrapper {
	// CONSTS
	private static final long EXPIRE_MILLIS = TimeUnit.HOURS.toMillis(1);
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGuildWrapper.class);

	// FIELDS
	private final GuildService guildService;
	private final GuildDomainFactory factory;
	private final LoadingCache<String, Guild> cache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MILLIS, TimeUnit.MILLISECONDS).build(new CacheLoader<String, Guild>() {
		@Override
		public Guild load(final String key) throws NoSuchGuildException {
			final Optional<GuildDetailsDTO> dto = DefaultGuildWrapper.this.guildService.retrieveGuildDetails(key);
			if (dto.isPresent()) {
				return DefaultGuildWrapper.this.factory.newGuildBuilder().id(dto.get().getId()).tag(dto.get().getTag()).name(dto.get().getName()).build();
			} else {
				throw new NoSuchGuildException();
			}
		}
	});

	// CONSTRUCTOR
	@Inject
	public DefaultGuildWrapper(final GuildService service, final GuildDomainFactory factory) {
		this.guildService = checkNotNull(service, "missing service");
		this.factory = checkNotNull(factory, "missing factory");
	}

	// METHODS

	@Override
	public Guild getGuild(final String id) throws NoSuchGuildException {
		checkNotNull(id, "missing id");
		try {
			return this.cache.get(id);
		} catch (ExecutionException e) {
			Throwables.propagateIfInstanceOf(e.getCause(), NoSuchGuildException.class);
			LOGGER.error("Failed to retrieve cached guild for id={}", id, e);
			throw Throwables.propagate(e);
		}
	}

	@Override
	public Optional<Guild> getGuildUnchecked(final String id) {
		checkNotNull(id, "missing id");
		try {
			return Optional.of(this.getGuild(id));
		} catch (NoSuchGuildException e) {
			return Optional.absent();
		}
	}
}
