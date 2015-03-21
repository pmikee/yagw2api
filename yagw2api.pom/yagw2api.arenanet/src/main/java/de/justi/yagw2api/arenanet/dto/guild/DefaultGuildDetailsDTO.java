package de.justi.yagw2api.arenanet.dto.guild;

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

import javax.annotation.concurrent.Immutable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

@Immutable
final class DefaultGuildDetailsDTO implements GuildDetailsDTO {
	@SerializedName("guild_id")
	@Since(1.0)
	private final String id = null;

	@SerializedName("guild_name")
	@Since(1.0)
	private final String name = null;

	@SerializedName("tag")
	@Since(1.0)
	private final String tag = null;

	@SerializedName("emblem")
	@Since(1.0)
	private final DefaultGuildEmblemDTO emblem = null;

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
	public Optional<GuildEmblemDTO> getEmblem() {
		return Optional.<GuildEmblemDTO> fromNullable(this.emblem);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.getId()).add("name", this.getName()).add("tag", this.getTag()).add("emblem", this.getEmblem()).toString();
	}
}
