package de.justi.yagw2api.arenanet.dto.map;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import de.justi.yagwapi.common.Tuple2;

class DelegatingMapContinent implements MapContinentDTO {
	// FIELDS
	private final MapContinentDTO delegate;

	// CONSTRUCTOR
	protected DelegatingMapContinent(final MapContinentDTO delegate) {
		this.delegate = checkNotNull(delegate, "missing delegate");
	}

	// METHODS
	public ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("delegate", this.delegate);
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getName()
	 */
	@Override
	public String getName() {
		return this.delegate.getName();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getDimension()
	 */
	@Override
	public Tuple2<Integer, Integer> getDimension() {
		return this.delegate.getDimension();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getMinZoom()
	 */
	@Override
	public int getMinZoom() {
		return this.delegate.getMinZoom();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getMaxZoom()
	 */
	@Override
	public int getMaxZoom() {
		return this.delegate.getMaxZoom();
	}

	/**
	 * @return
	 * @see de.justi.yagw2api.arenanet.dto.map.MapContinentDTO#getFloors()
	 */
	@Override
	public Set<Integer> getFloors() {
		return this.delegate.getFloors();
	}
}
