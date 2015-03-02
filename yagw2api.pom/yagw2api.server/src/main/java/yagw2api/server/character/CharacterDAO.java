package yagw2api.server.character;

import com.google.common.base.Optional;

public interface CharacterDAO {

	Optional<Character> findCharacter(final String name);

	Iterable<Character> getAllCharacter();

	Character update(final Character character);
}
