package de.justi.yagw2api.mumblelink;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-MumbleLink
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.mumblelink.impl.Module;

public enum YAGW2APIMumbleLink {
	INSTANCE;

	private final Injector injector;
	private final IMumbleLink mumbleLink;

	private YAGW2APIMumbleLink() {
		this.injector = Guice.createInjector(new Module());
		checkState(this.injector != null);
		this.mumbleLink = checkNotNull(this.injector.getInstance(IMumbleLink.class));
	}

	public IMumbleLink getMumbleLink() {
		return this.mumbleLink;
	}
}
