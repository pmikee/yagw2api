package api.model;

import java.util.Locale;

public interface IWVWObjective extends IHasWVWLocation, IHasChannel {
	
	String getName(Locale locale);
	IWVWObjectiveType getType();
}
