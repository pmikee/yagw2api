package yagw2api.server.character;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

@Component
@Scope(value = "application")
public final class InMemoryCharacterDAO implements CharacterDAO {

	private final Map<String, Character> characterStore = Maps.newConcurrentMap();

	@Override
	public synchronized Optional<Character> findCharacter(final String name) {
		checkNotNull(name, "missing name");
		if (this.characterStore.containsKey(name)) {
			return Optional.of(this.characterStore.get(name));
		} else {
			return Optional.absent();
		}
	}

	@Override
	public synchronized Iterable<Character> getAllCharacter() {
		return ImmutableList.copyOf(this.characterStore.values());
	}

	@Override
	public synchronized Character update(final Character character) {
		checkNotNull(character, "missing character");
		this.characterStore.put(character.getName(), character);
		return character;
	}

}
