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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static de.justi.yagw2api.wrapper.map.domain.impl.MapConstants.TILE_SIZE;
import de.justi.yagw2api.common.tuple.Tuples;
import de.justi.yagw2api.common.tuple.UniformNumberTuple2;
import de.justi.yagw2api.common.tuple.UniformNumberTuple4;

final class MapUtils {
	// CONSTS

	// STATIC METHODS
	static int getTileTextureSize(final int zoom, final int minZoom, final int maxZoom) {
		checkArgument(zoom >= minZoom && zoom <= maxZoom, "invalid zoom=%s for %s", zoom);
		return TILE_SIZE * (int) Math.pow(2, maxZoom - zoom);
	}

	static final UniformNumberTuple2<Integer> fromLatLngToPoint(final double lat, final double lng, final int maxZoom){
		final int tiles = 1 << maxZoom;
		final double sin_y = de.justi.yagw2api.common.math.Math.clamp(Math.sin(lng * (Math.PI / 180)), -0.9999, 0.9999);
		final double x = 128 + lat * (256 / 360);
		final double y = 128 + 0.5 * Math.log((1 + sin_y) / (1 - sin_y)) * -(256 / (2 * Math.PI));
		return Tuples.uniformOf((int) Math.floor(x * tiles), (int) Math.floor(y * tiles));
	}
	/**
	 * translate lat/lng coordinates to pixel values
	 *
	 * @param ll
	 * @param max_zoom
	 * @returns
	 */
	static final UniformNumberTuple2<Integer> fromLatLngToPoint(final UniformNumberTuple2<Double> ll, final int maxZoom) {
		checkNotNull(ll,"missing ll");
		return fromLatLngToPoint(ll.v1(), ll.v2(), maxZoom);
	}

	static final UniformNumberTuple4<Integer> fromLatLngToPoint(final UniformNumberTuple4<Double> llll, final int maxZoom) {
		checkNotNull(llll,"missing llll");
		final UniformNumberTuple2<Integer> first = fromLatLngToPoint(llll.v1(),llll.v2(), maxZoom);
		final UniformNumberTuple2<Integer> second = fromLatLngToPoint(llll.v3(),llll.v4(), maxZoom);
		return Tuples.uniformOf(first.v1(), first.v2(), second.v1(), second.v2());
	}

	static final UniformNumberTuple2<Double> fromPointToLatLng(final int x, final int y, final int maxZoom) {
		final int size = (1 << maxZoom) * 256;
		final double lat = (2 * Math.atan(Math.exp((y - size / 2) / -(size / (2 * Math.PI)))) - (Math.PI / 2)) * (180 / Math.PI);
		final double lng = (x - size / 2) * (360 / size);
		return Tuples.uniformOf(lat, lng);
	}

	/**
	 * translate pixel values to GMaps lat/lng
	 *
	 * @param point
	 * @param max_zoom
	 * @returns
	 */
	static final UniformNumberTuple2<Double> fromPointToLatLng(final UniformNumberTuple2<Integer> point, final int maxZoom) {
		checkNotNull(point,"missing point");
		return fromPointToLatLng(point.v1(), point.v2(), maxZoom);
	}
	static final UniformNumberTuple4<Double> fromPointToLatLng(final UniformNumberTuple4<Integer> points, final int maxZoom) {
		checkNotNull(points,"missing points");
		final UniformNumberTuple2<Double> first = fromPointToLatLng(points.v1(),points.v2(), maxZoom);
		final UniformNumberTuple2<Double> second = fromPointToLatLng(points.v3(),points.v4(), maxZoom);
		return Tuples.uniformOf(first.v1(), first.v2(), second.v1(), second.v2());
	}
	// CONSTRUCTOR
	private MapUtils() {
		throw new AssertionError("no instance");
	}
}
