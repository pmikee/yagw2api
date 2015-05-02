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

import java.util.Arrays;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

final class DefaultGuildEmblemDTO implements GuildEmblemDTO {

	@SerializedName("background_id")
	@Since(1.0)
	private int backgroundId;

	@SerializedName("foreground_id")
	@Since(1.0)
	private int foregroundId;

	@SerializedName("flags")
	@Since(1.0)
	private String[] flags;

	@SerializedName("background_color_id")
	@Since(1.0)
	private int backgroundColorId;

	@SerializedName("foreground_primary_color_id")
	@Since(1.0)
	private int foregroundPrimaryColorId;

	@SerializedName("foreground_secondary_color_id")
	@Since(1.0)
	private int foregroundSecondaryColorId;

	@Override
	public int getBackgroundId() {
		return this.backgroundId;
	}

	@Override
	public int getForegroundId() {
		return this.foregroundId;
	}

	@Override
	public int getBackgroundColorId() {
		return this.backgroundColorId;
	}

	@Override
	public int getForegroundPrimaryColorId() {
		return this.foregroundPrimaryColorId;
	}

	@Override
	public int getForegroundSecondaryColorId() {
		return this.foregroundSecondaryColorId;
	}

	@Override
	public String[] getFlags() {
		return this.flags;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("backgroundId", this.getBackgroundId()).add("foregroundId", this.getForegroundId())
				.add("backgroundColorId", this.getBackgroundColorId()).add("foregroundPrimaryColorId", this.getForegroundPrimaryColorId())
				.add("foregroundSecondaryColorId", this.getForegroundSecondaryColorId()).add("flags", Arrays.deepToString(this.flags)).toString();
	}
}
