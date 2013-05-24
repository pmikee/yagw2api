package api.model;

import com.google.common.eventbus.EventBus;

public interface IHasChannel {
	EventBus getChannel();
}
