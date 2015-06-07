package de.justi.yagw2api.common.math;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
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


public final class Math {
	private Math() {
		throw new AssertionError("no instance");
	}

	public static final int clamp(final int toClamp, final int min, final int max) {
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

	public static final long clamp(final long toClamp, final long min, final long max) {
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

	public static final double clamp(final double toClamp, final double min, final double max) {
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

	public static final float clamp(final float toClamp, final float min, final float max) {
		return java.lang.Math.min(java.lang.Math.max(min, toClamp), max);
	}

}
