package de.justi.yagw2api.explorer.rcp.map;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-RCP-Application World-Map
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Range;

final class ZoomManager extends AbstractValueManager {
	// CONSTS
	static final Logger LOGGER = LoggerFactory.getLogger(ZoomManager.class);
	private final ZoomManager.ZoomChangedCallback callback;

	// EMBEDDED
	public static interface ZoomChangedCallback {
		void onZoomChanged(int oldZoom, int newZoom);
	}

	public ZoomManager(final ZoomManager.ZoomChangedCallback callback) {
		super(Range.closed(100, 775));
		this.callback = checkNotNull(callback, "missing callback");
	}

	@Override
	protected void onValueChanged(final int oldValue, final int newValue) {
		this.callback.onZoomChanged(oldValue, newValue);
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("callback", this.callback);
	}

	@Override
	protected int getDefaultValue() {
		return 100;
	}
}