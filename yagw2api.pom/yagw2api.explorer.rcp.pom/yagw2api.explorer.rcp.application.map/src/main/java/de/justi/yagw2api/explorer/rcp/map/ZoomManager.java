package de.justi.yagw2api.explorer.rcp.map;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Range;

final class ZoomManager extends AbstractValueManager {
	// CONSTS
	static final Logger LOGGER = LoggerFactory.getLogger(ZoomManager.class);
	private final ZoomManager.ZoomChangedCallback callback;

	// EMBEDDED
	public static interface ZoomChangedCallback {
		void onZoomChanged(int oldZoom, int newZoom);
	}

	public ZoomManager(final ZoomManager.ZoomChangedCallback callback) {
		super(Range.closed(1, 7));
		this.callback = checkNotNull(callback, "missing callback");
	}

	@Override
	protected void onValueChanged(final int oldValue, final int newValue) {
		this.callback.onZoomChanged(oldValue, newValue);
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("callback", this.callback);
	}

	@Override
	protected int getDefaultValue() {
		return 1;
	}
}