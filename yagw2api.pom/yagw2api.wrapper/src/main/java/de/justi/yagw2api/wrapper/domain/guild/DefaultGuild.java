package de.justi.yagw2api.wrapper.domain.guild;

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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import de.justi.yagwapi.common.Unmodifiable;

public final class DefaultGuild implements Guild, Unmodifiable {
	private final String id;
	private final String name;
	private final String tag;

	public DefaultGuild(final String id, final String name, final String tag) {
		this.id = checkNotNull(id);
		this.name = checkNotNull(name);
		this.tag = checkNotNull(tag);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getClass().getName(), this.id);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Guild)) {
			return false;
		} else {
			final Guild guild = (Guild) obj;
			return Objects.equal(this.id, guild.getId());
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getTag() {
		return this.tag;
	}
}
