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

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import de.justi.yagw2api.wrapper.map.domain.MapTile;
import de.justi.yagw2api.wrapper.map.domain.MapTile.MapTileBuilder;
import de.justi.yagwapi.common.tuple.UniformNumberTuple2;

// EMBEDDED
abstract class AbstractMapTileBuilder<B extends AbstractMapTileBuilder<B>> implements MapTileBuilder {

	// FIELDS
	@Nullable
	private UniformNumberTuple2<Integer> position = null;
	@Nullable
	private Integer floorIndex = null;
	@Nullable
	private Integer zoom = null;
	@Nullable
	private String continentId = null;

	// CONSTRUCTOR
	protected AbstractMapTileBuilder() {
	}

	// METHODS
	protected abstract B self();

	@Override
	public abstract MapTile build();

	@Override
	public final B position(@Nullable final UniformNumberTuple2<Integer> position) {
		this.position = position;
		return this.self();
	}

	@Override
	public final B floorIndex(final int floorIndex) {
		this.floorIndex = floorIndex;
		return this.self();
	}

	@Override
	public final B continentId(@Nullable final String continentId) {
		this.continentId = continentId;
		return this.self();
	}

	@Override
	public final B zoom(final int zoom) {
		this.zoom = zoom;
		return this.self();
	}

	/**
	 * @return the position
	 */
	protected final UniformNumberTuple2<Integer> getPosition() {
		return this.position;
	}

	/**
	 * @return the floorIndex
	 */
	protected final Integer getFloorIndex() {
		return this.floorIndex;
	}

	/**
	 * @return the zoom
	 */
	protected final Integer getZoom() {
		return this.zoom;
	}

	/**
	 * @return the continentId
	 */
	protected final String getContinentId() {
		return this.continentId;
	}

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("position", this.position).add("floorIndex", this.floorIndex).add("zoom", this.zoom).add("continentId", this.continentId);
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}
}