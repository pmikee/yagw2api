package de.justi.yagw2api.explorer.rcp.map;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Spinner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Range;

final class FloorManager {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(FloorManager.class);

	// EMBEDDED
	public static interface FloorChangedCallback {
		void onFloorChanged(int oldFloor, int newFloor);
	}

	// FIELDS
	private Range<Integer> validFloorRange = Range.closed(-10, 10);
	private int currentFloor = 0;

	private final Spinner control;
	private final FloorManager.FloorChangedCallback callback;

	public FloorManager(final Spinner control, final FloorManager.FloorChangedCallback callback) {
		this.control = checkNotNull(control, "missing increaseSource");
		this.callback = checkNotNull(callback, "missing callback");
		this.control.setMinimum(this.validFloorRange.lowerEndpoint());
		this.control.setMaximum(this.validFloorRange.upperEndpoint());
		this.control.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				FloorManager.this.updateFloor(control.getSelection());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				FloorManager.this.updateFloor(control.getSelection());
			}
		});
		this.updateFloor(1);
	}

	private void updateFloor(final int newFloor) {
		final int oldFloor = this.currentFloor;
		if (this.validFloorRange.contains(newFloor)) {
			this.currentFloor = newFloor;
		} else if (this.validFloorRange.hasLowerBound() && newFloor < this.validFloorRange.lowerEndpoint()) {
			this.currentFloor = this.validFloorRange.lowerEndpoint();
		} else if (this.validFloorRange.hasUpperBound() && newFloor > this.validFloorRange.upperEndpoint()) {
			this.currentFloor = this.validFloorRange.upperEndpoint();
		} else {
			// should never occur, because validFloorRange#contains should take everything that is in range
			throw new IllegalStateException("Unexpected floor=" + newFloor + " where valid range is " + this.validFloorRange);
		}
		if (oldFloor != this.currentFloor) {
			LOGGER.info("changed floor from {} to {}", oldFloor, this.currentFloor);
			this.callback.onFloorChanged(oldFloor, this.currentFloor);
		}
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this).add("currentFloor", this.currentFloor).add("validFloorRange", this.validFloorRange).add("control", this.control)
				.add("callback", this.callback).toString();
	}

}
