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
import de.justi.yagw2api.wrapper.map.domain.POILandmark;

final class DefaultPOILandmark extends AbstractPOI implements POILandmark {
	// STATICS
	public static final POILandmarkBuilder builder() {
		return new DefaultPOILandmarkBuilder();
	}

	// EMBEDDED
	private static final class DefaultPOILandmarkBuilder extends AbstractPOIBuilder<POILandmark, POILandmarkBuilder> implements POILandmarkBuilder {

		@Override
		public DefaultPOILandmark build() {
			return new DefaultPOILandmark(this);
		}

		@Override
		protected DefaultPOILandmarkBuilder self() {
			return this;
		}

	}

	// FIELDS

	// CONSTRUCTOR
	private DefaultPOILandmark(final DefaultPOILandmarkBuilder builder) {
		super(checkNotNull(builder, "missing builder"));
	}

}
