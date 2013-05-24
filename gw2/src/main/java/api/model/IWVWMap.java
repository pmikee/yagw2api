package api.model;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;

public interface IWVWMap extends IHasChannel {
	IWVWMapType getType();
	Map<IWVWLocationType, IHasWVWLocation> getMappedByPosition();
	Set<IHasWVWLocation> getEverything();
	Set<IWVWObjective> getObjectives();
	Optional<IHasWVWLocation> getByLocation(IWVWLocationType location);
}