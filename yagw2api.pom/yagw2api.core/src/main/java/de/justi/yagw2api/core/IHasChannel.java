package de.justi.yagw2api.core;

import com.google.common.eventbus.EventBus;

public interface IHasChannel {
	EventBus getChannel();
}
