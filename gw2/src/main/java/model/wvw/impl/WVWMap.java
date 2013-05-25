package model.wvw.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import model.AbstractHasChannel;
import model.wvw.IHasWVWLocation;
import model.wvw.IWVWLocationType;
import model.wvw.IWVWMap;
import model.wvw.IWVWMapType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import model.wvw.IWVWScores;
import utils.InjectionHelper;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

class WVWMap extends AbstractHasChannel implements IWVWMap {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);

	private final IWVWMapType type;
	private final Map<IWVWLocationType, IHasWVWLocation> content;
	private final IWVWScores scores;

	public WVWMap(IWVWMapType type, Collection<IHasWVWLocation> contents) {
		checkNotNull(type);
		checkNotNull(contents);
		checkArgument(contents.size() > 0);

		this.type = type;
		final ImmutableMap.Builder<IWVWLocationType, IHasWVWLocation> contentBuilder = ImmutableMap.builder();
		for (IHasWVWLocation content : contents) {
			contentBuilder.put(content.getLocation(), content);
		}
		this.content = contentBuilder.build();
		this.scores = WVW_MODEL_FACTORY.createScores();
	}

	public IWVWMapType getType() {
		return this.type;
	}

	public Map<IWVWLocationType, IHasWVWLocation> getMappedByPosition() {
		return this.content;
	}

	public Set<IHasWVWLocation> getEverything() {
		return ImmutableSet.copyOf(this.content.values());
	}

	public Set<IWVWObjective> getObjectives() {
		return ImmutableSet.copyOf(Iterables.filter(this.content.values(), IWVWObjective.class));
	}

	public Optional<IHasWVWLocation> getByLocation(IWVWLocationType location) {
		if (this.content.containsKey(location)) {
			return Optional.fromNullable(this.content.get(location));
		} else {
			return Optional.absent();
		}
	}

	public String toString() {
		return Objects.toStringHelper(this).add("type", this.type).add("content", this.content).toString();
	}

	@Override
	public IWVWScores getScores() {
		return this.scores;
	}

}
