package de.justi.yagw2api.core.wrapper.model;

import com.google.common.eventbus.EventBus;

public interface IHasChannel {
	EventBus getChannel();
}
