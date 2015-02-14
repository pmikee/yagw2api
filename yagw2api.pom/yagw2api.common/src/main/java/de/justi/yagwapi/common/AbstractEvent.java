package de.justi.yagwapi.common;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
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


import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;


public abstract class AbstractEvent implements IEvent {
	private final LocalDateTime timestamp;

	public AbstractEvent() {
		this.timestamp = LocalDateTime.now();
	}

	public final LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	public String toString() {
		return MoreObjects.toStringHelper(this).add("timestamp", timestamp).toString();
	}
}
