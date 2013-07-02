package de.justi.yagwapi.common;

import com.google.common.eventbus.EventBus;

public interface IHasChannel {
	EventBus getChannel();
}
