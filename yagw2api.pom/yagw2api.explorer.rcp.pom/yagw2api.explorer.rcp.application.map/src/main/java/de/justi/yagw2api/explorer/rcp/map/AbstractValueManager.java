package de.justi.yagw2api.explorer.rcp.map;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

abstract class AbstractValueManager {

	private final Range<Integer> validRange;
	private int currentValue;

	public AbstractValueManager(final Range<Integer> validRange) {
		this.validRange = checkNotNull(validRange, "missing valid range");
	}

	public final void reset() {
		this.updateValue(this.getDefaultValue());
	}

	protected abstract int getDefaultValue();

	public final synchronized int decrement() {
		return this.updateValue(this.currentValue - 1);
	}

	public final synchronized int increment() {
		return this.updateValue(this.currentValue + 1);
	}

	private final boolean isOutOfLowerRange(final int value) {
		return (this.validRange.hasLowerBound() && ((this.validRange.lowerBoundType().equals(BoundType.OPEN) && value < this.validRange.lowerEndpoint()) || (this.validRange
				.lowerBoundType().equals(BoundType.CLOSED) && value <= this.validRange.lowerEndpoint())));
	}

	private final boolean isOutOfUpperRange(final int value) {
		return (this.validRange.hasUpperBound() && ((this.validRange.upperBoundType().equals(BoundType.OPEN) && value < this.validRange.upperEndpoint()) || (this.validRange
				.upperBoundType().equals(BoundType.CLOSED) && value <= this.validRange.upperEndpoint())));
	}

	public final synchronized int updateValue(final int newValue) {
		final int oldValue = this.currentValue;
		if (this.validRange.contains(newValue)) {
			this.currentValue = newValue;
		} else if (this.isOutOfLowerRange(newValue)) {
			this.currentValue = this.validRange.lowerEndpoint();
		} else if (this.isOutOfUpperRange(newValue)) {
			this.currentValue = this.validRange.upperEndpoint();
		} else {
			// should never occur, because validZoomRange#contains should take everything that is in range
			throw new IllegalStateException("Unexpected value=" + newValue + " where valid range is " + this.validRange + " for " + this);
		}
		if (oldValue != this.currentValue) {
			LoggerFactory.getLogger(this.getClass()).info("changed value of {} from {} to {}", this, oldValue, this.currentValue);
			this.onValueChanged(oldValue, this.currentValue);
		}
		return this.currentValue;
	}

	protected abstract void onValueChanged(int oldValue, int newValue);

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("currentValue", this.currentValue).add("validRange", this.validRange);
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

}