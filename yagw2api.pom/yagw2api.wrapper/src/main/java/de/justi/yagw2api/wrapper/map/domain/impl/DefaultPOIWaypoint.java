package de.justi.yagw2api.wrapper.map.domain.impl;

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
import de.justi.yagw2api.wrapper.map.domain.POIWaypoint;

final class DefaultPOIWaypoint extends AbstractPOI implements POIWaypoint {
	// STATICS
	public static final POIWaypointBuilder builder() {
		return new DefaultPOIWaypointBuilder();
	}

	// EMBEDDED
	private static final class DefaultPOIWaypointBuilder extends AbstractPOIBuilder<POIWaypoint, POIWaypointBuilder> implements POIWaypointBuilder {

		@Override
		public DefaultPOIWaypoint build() {
			return new DefaultPOIWaypoint(this);
		}

		@Override
		protected DefaultPOIWaypointBuilder self() {
			return this;
		}

	}

	// FIELDS

	// CONSTRUCTOR
	private DefaultPOIWaypoint(final DefaultPOIWaypointBuilder builder) {
		super(checkNotNull(builder, "missing builder"));
	}

}
