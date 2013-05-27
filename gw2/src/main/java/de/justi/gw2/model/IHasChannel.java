package de.justi.gw2.model;

import com.google.common.eventbus.EventBus;

public interface IHasChannel {
	EventBus getChannel();
}
