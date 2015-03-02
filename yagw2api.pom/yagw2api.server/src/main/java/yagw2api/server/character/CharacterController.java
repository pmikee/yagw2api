package yagw2api.server.character;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import yagw2api.server.character.Character.CharacterBuilder;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

@RestController
@RequestMapping("/character")
public final class CharacterController {

	private final CharacterDAO dao;

	@Autowired
	public CharacterController(final CharacterDAO dao) {
		this.dao = checkNotNull(dao, "missing dao");
	}

	@RequestMapping(value = "/all", method = { RequestMethod.GET })
	public Character[] getAllCharacters() {
		return Iterables.toArray(this.dao.getAllCharacter(), Character.class);
	}

	@RequestMapping(value = "/{characterName}", method = { RequestMethod.GET })
	public Character getCharacter(@PathVariable final String characterName) {
		checkNotNull(characterName, "missing characterName");
		return this.dao.findCharacter(characterName).orNull();
	}

	@RequestMapping(value = "/{characterName}", method = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH })
	public Character updateCharacter(
	//@formatter:off
			@PathVariable("characterName") final String name,
			@RequestParam(value="worldId",required=true) final int worldId,
			@RequestParam(value="mapId",required=true) final int mapId,
			@RequestParam(value="commander",required=true) final boolean commander,
			@RequestParam(value="x",required=true) final float xPosition,
			@RequestParam(value="y",required=true) final float yPosition,
			@RequestParam(value="z",required=true) final float zPosition)
	//@formatter:on
	{
		checkNotNull(name, "missing name");
		final Optional<Character> found = this.dao.findCharacter(name);
		final CharacterBuilder resultBuilder;
		if (found.isPresent()) {
			resultBuilder = found.get().toBuilder();
		} else {
			resultBuilder = Character.builder().name(name);
		}
		resultBuilder.mapId(mapId).worldId(worldId).commander(commander).position(xPosition, yPosition, zPosition);
		return this.dao.update(resultBuilder.build());
	}
}
