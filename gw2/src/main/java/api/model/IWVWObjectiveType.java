package api.model;

import java.util.concurrent.TimeUnit;

public interface IWVWObjectiveType {
	String getLabel();
	long getBuffDuration(TimeUnit timeUnit);
	int getPoints();
}
