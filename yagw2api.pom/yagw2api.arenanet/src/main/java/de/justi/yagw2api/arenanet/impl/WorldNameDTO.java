package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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


import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IWorldNameDTO;

final class WorldNameDTO implements IWorldNameDTO {
	private static final transient Pattern PATTERN = Pattern.compile("([^\\[]*)((" + Pattern.quote("[") + "[A-Z]*" + Pattern.quote("]") + ")?)");
	@Since(1.0)
	@SerializedName("id")
	int id;
	@Since(1.0)
	@SerializedName("name")
	String name;

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).add("locale", this.getWorldLocale()).add("nameWithoutLocale", this.getNameWithoutLocale()).toString();
	}

	@Override
	public Optional<Locale> getWorldLocale() {
		final Matcher matcher = PATTERN.matcher(this.getName());
		if (matcher.find()) {
			if (matcher.group(3) != null) {
				String localeTag = matcher.group(3).replaceAll("\\[", "").replaceAll("\\]", "").trim().toLowerCase();
				switch (localeTag) {
					case "sp":
						localeTag = "es";
						break;
				}
				return Optional.of(Locale.forLanguageTag(localeTag));
			} else {
				return Optional.absent();
			}
		} else {
			return Optional.absent();
		}
	}

	@Override
	public String getNameWithoutLocale() {
		final Matcher matcher = PATTERN.matcher(this.getName());
		if (matcher.find()) {
			return matcher.group(1).trim();
		} else {
			return this.getName();
		}
	}

	@Override
	public boolean isEurope() {
		return (this.id >= 2000) && (this.id <= 3000);
	}

	@Override
	public boolean isNorthAmerica() {
		return (this.id >= 1000) && (this.id <= 2000);
	}
}
