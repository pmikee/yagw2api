package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.concurrent.TimeUnit;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import de.justi.yagw2api.wrapper.IWVWObjectiveType;

public enum WVWObjectiveType implements IWVWObjectiveType {
	RUINS(0, TimeUnit.MINUTES, 0), CAMP(5, TimeUnit.MINUTES, 5), TOWER(5, TimeUnit.MINUTES, 10), KEEP(5, TimeUnit.MINUTES, 25), CASTLE(5, TimeUnit.MINUTES, 35);

	private final long buffDurationMillis;
	private final int points;

	private WVWObjectiveType(long buffDuration, TimeUnit buffDurationTimeUnit, int points) {
		checkArgument(buffDuration >= 0);
		checkNotNull(buffDurationTimeUnit);
		this.buffDurationMillis = buffDurationTimeUnit.toMillis(buffDuration);
		checkArgument(points >= 0);
		this.points = points;
	}

	@Override
	public String getLabel() {
		return this.name();
	}

	@Override
	public long getBuffDuration(TimeUnit timeUnit) {
		checkNotNull(timeUnit);
		checkState(this.buffDurationMillis >= 0);
		return timeUnit.convert(this.buffDurationMillis, TimeUnit.MILLISECONDS);
	}

	@Override
	public int getPoints() {
		return this.points;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("label", this.getLabel()).add("buffDuration", this.buffDurationMillis + "ms").add("points", this.points).toString();
	}

	@Override
	public boolean isCamp() {
		return this.equals(CAMP);
	}

	@Override
	public boolean isTower() {
		return this.equals(TOWER);
	}

	@Override
	public boolean isKeep() {
		return this.equals(KEEP);
	}

	@Override
	public boolean isCastle() {
		return this.equals(CASTLE);
	}
}
