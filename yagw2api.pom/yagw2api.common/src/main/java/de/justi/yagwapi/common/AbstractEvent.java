package de.justi.yagwapi.common;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
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


import java.text.DateFormat;
import java.util.Calendar;

import com.google.common.base.Objects;


public abstract class AbstractEvent implements IEvent {
	private final Calendar timestamp;

	public AbstractEvent() {
		this.timestamp = Calendar.getInstance();
	}

	public final Calendar getTimestamp() {
		return this.timestamp;
	}

	public String toString() {
		final DateFormat df = DateFormat.getDateTimeInstance();
		return Objects.toStringHelper(this).add("timestamp", df.format(this.timestamp.getTime())).toString();
	}
}
