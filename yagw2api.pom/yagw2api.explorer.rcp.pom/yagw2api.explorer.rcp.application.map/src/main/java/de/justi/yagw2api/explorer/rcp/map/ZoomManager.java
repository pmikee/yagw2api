package de.justi.yagw2api.explorer.rcp.map;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Range;

final class ZoomManager extends SelectionAdapter {
	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(ZoomManager.class);

	// EMBEDDED
	public static interface ZoomChangedCallback {
		void onZoomLevelChanged(int oldZoom, int newZoom);
	}

	// FIELDS
	private Range<Integer> validZoomRange = Range.closed(1, 7);
	private int currentZoom = 0;

	private final Button increaseSource;
	private final Button decreaseSource;
	private final ZoomManager.ZoomChangedCallback callback;

	public ZoomManager(final Button increaseSource, final Button decreaseSource, final ZoomManager.ZoomChangedCallback callback) {
		this.increaseSource = checkNotNull(increaseSource, "missing increaseSource");
		this.decreaseSource = checkNotNull(decreaseSource, "missing decreaseSource");
		this.callback = checkNotNull(callback, "missing callback");
		this.increaseSource.addSelectionListener(this);
		this.decreaseSource.addSelectionListener(this);
		this.updateZoom(1);
	}

	private void updateZoom(final int newZoom) {
		final int oldZoom = this.currentZoom;
		if (this.validZoomRange.contains(newZoom)) {
			this.currentZoom = newZoom;
		} else if (this.validZoomRange.hasLowerBound() && newZoom < this.validZoomRange.lowerEndpoint()) {
			this.currentZoom = this.validZoomRange.lowerEndpoint();
		} else if (this.validZoomRange.hasUpperBound() && newZoom > this.validZoomRange.upperEndpoint()) {
			this.currentZoom = this.validZoomRange.upperEndpoint();
		} else {
			// should never occur, because validZoomRange#contains should take everything that is in range
			throw new IllegalStateException("Unexpected zoom=" + newZoom + " where valid range is " + this.validZoomRange);
		}
		if (oldZoom != this.currentZoom) {
			LOGGER.info("changed zoom level from {} to {}", oldZoom, this.currentZoom);
			this.callback.onZoomLevelChanged(oldZoom, this.currentZoom);
		}
	}

	@Override
	public void widgetSelected(final SelectionEvent e) {
		if (this.increaseSource.equals(e.getSource())) {
			this.updateZoom(this.currentZoom + 1);
		} else if (this.decreaseSource.equals(e.getSource())) {
			this.updateZoom(this.currentZoom - 1);
		}
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this).add("currentZoom", this.currentZoom).add("validZoomRange", this.validZoomRange).add("incSource", this.increaseSource)
				.add("decSource", this.decreaseSource).add("callback", this.callback).toString();
	}
}