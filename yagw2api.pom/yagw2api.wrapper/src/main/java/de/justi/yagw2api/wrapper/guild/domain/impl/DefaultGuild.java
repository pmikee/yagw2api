package de.justi.yagw2api.wrapper.guild.domain.impl;

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

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import de.justi.yagw2api.wrapper.guild.domain.Guild;
import de.justi.yagw2api.wrapper.guild.domain.Guild.GuildBuilder;
import de.justi.yagwapi.common.Unmodifiable;

public final class DefaultGuild implements Guild, Unmodifiable {
	// STATIC
	public static final GuildBuilder builder() {
		return new DefaultGuildBuilder();
	}

	// EMBEDDED
	private static final class DefaultGuildBuilder implements GuildBuilder {

		// FIELDS
		@Nullable
		private String id = null;
		@Nullable
		private String name = null;
		@Nullable
		private String tag = null;

		// CONSTRUCTOR
		private DefaultGuildBuilder() {
		}

		// METHODS
		@Override
		public GuildBuilder id(@Nullable final String id) {
			this.id = id;
			return this;
		}

		@Override
		public GuildBuilder name(@Nullable final String name) {
			this.name = name;
			return this;
		}

		@Override
		public GuildBuilder tag(@Nullable final String tag) {
			this.tag = tag;
			return this;
		}

		@Override
		public Guild build() {
			return new DefaultGuild(this);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("tag", this.tag).toString();
		}
	}

	// FIELDS
	private final String id;
	private final String name;
	private final String tag;

	// CONSTRUCTOR
	private DefaultGuild(final DefaultGuildBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.name = checkNotNull(builder.name, "missing name in %s", builder);
		this.tag = checkNotNull(builder.tag, "missing tag in %s", builder);
	}

	// METHODS

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getTag() {
		return this.tag;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("tag", this.tag).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.tag == null) ? 0 : this.tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultGuild))
			return false;
		DefaultGuild other = (DefaultGuild) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.tag == null) {
			if (other.tag != null)
				return false;
		} else if (!this.tag.equals(other.tag))
			return false;
		return true;
	}
}
