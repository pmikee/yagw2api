package de.justi.yagw2api.arenanet.v1.dto.map;

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

import java.util.Collections;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

final class DefaultMapsDTO implements MapsDTO {

	@SerializedName("maps")
	@Since(1.0)
	private final Map<String, DefaultMapsMapDTO> maps = ImmutableMap.of();

	@Override
	public Map<String, MapsMapDTO> getMaps() {
		return Collections.unmodifiableMap(this.maps);
	}

	@Override
	public Optional<MapsMapDTO> getMap(final String mapId) {
		checkNotNull(mapId, "missing mapId");
		return this.maps.containsKey(mapId) ? Optional.of(this.maps.get(mapId)) : Optional.absent();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("maps", this.maps).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.maps == null) ? 0 : this.maps.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DefaultMapsDTO)) {
			return false;
		}
		DefaultMapsDTO other = (DefaultMapsDTO) obj;
		if (this.maps == null) {
			if (other.maps != null) {
				return false;
			}
		} else if (!this.maps.equals(other.maps)) {
			return false;
		}
		return true;
	}
}
