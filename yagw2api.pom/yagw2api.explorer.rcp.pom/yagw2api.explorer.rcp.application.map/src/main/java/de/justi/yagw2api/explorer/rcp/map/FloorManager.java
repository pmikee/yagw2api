package de.justi.yagw2api.explorer.rcp.map;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Range;

final class FloorManager extends AbstractValueManager {
	// CONSTS
	static final Logger LOGGER = LoggerFactory.getLogger(ZoomManager.class);
	private final FloorManager.FloorChangedCallback callback;

	// EMBEDDED
	public static interface FloorChangedCallback {
		void onFloorChanged(int oldFloor, int newFloor);
	}

	// CONSTRUCTOR
	public FloorManager(final FloorManager.FloorChangedCallback callback) {
		super(Range.closed(-5, 5));
		this.callback = checkNotNull(callback, "missing callback");
	}

	// METHODS
	@Override
	protected void onValueChanged(final int oldValue, final int newValue) {
		this.callback.onFloorChanged(oldValue, newValue);
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
