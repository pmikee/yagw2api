package de.justi.yagw2api.arenanet;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
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

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.dto.guild.GuildDetailsDTO;

public class DelegatingGuildService implements GuildService {
	// FIELDS
	private final GuildService delegate;

	// CONSTRUCTOR
	public DelegatingGuildService(final GuildService delegate) {
		this.delegate = checkNotNull(delegate, "missing delegate");
	}

	// METHODS
	protected final GuildService getDelegate() {
		return this.delegate;
	}

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).addValue(this.delegate);
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

	@Override
	public Optional<GuildDetailsDTO> retrieveGuildDetails(final String id) {
		return this.delegate.retrieveGuildDetails(id);
	}

}
