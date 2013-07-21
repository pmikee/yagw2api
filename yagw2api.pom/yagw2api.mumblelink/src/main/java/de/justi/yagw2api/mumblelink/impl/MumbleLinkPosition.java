package de.justi.yagw2api.mumblelink.impl;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;

final class MumbleLinkPosition implements IMumbleLinkPosition {
	static Optional<IMumbleLinkPosition> of(float[] positionArray) {
		if (positionArray == null) {
			return Optional.absent();
		} else {
			checkArgument(positionArray.length == 3);
			return Optional.<IMumbleLinkPosition> of(new MumbleLinkPosition(positionArray[0], positionArray[1], positionArray[2]));
		}
	}

	private final float x;
	private final float y;
	private final float z;

	private MumbleLinkPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public float getZ() {
		return this.z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Float.floatToIntBits(x);
		result = (prime * result) + Float.floatToIntBits(y);
		result = (prime * result) + Float.floatToIntBits(z);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MumbleLinkPosition)) {
			return false;
		}
		MumbleLinkPosition other = (MumbleLinkPosition) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("x", this.x).add("y", this.y).add("z", this.z).toString();
	}
}