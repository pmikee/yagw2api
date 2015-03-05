package yagw2api.server.character;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.server
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import yagw2api.server.character.Avatar.AvatarBuilder;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

@RestController
@RequestMapping("/character")
public final class AvatarController {

	private final AvatarDAO dao;

	@Autowired
	public AvatarController(final AvatarDAO dao) {
		this.dao = checkNotNull(dao, "missing dao");
	}

	@RequestMapping(value = "/all", method = { RequestMethod.GET })
	public Avatar[] getAll() {
		return Iterables.toArray(this.dao.getAll(), Avatar.class);
	}

	@RequestMapping(value = "/{characterName}", method = { RequestMethod.GET })
	public Avatar get(@PathVariable final String name) {
		checkNotNull(name, "missing name");
		return this.dao.find(name).orNull();
	}

	@RequestMapping(value = "/{name}", method = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH })
	public Avatar updateCharacter(
	//@formatter:off
			@PathVariable("name") final String name,
			@RequestParam(value="worldId",required=true) final int worldId,
			@RequestParam(value="mapId",required=true) final int mapId,
			@RequestParam(value="commander",required=true) final boolean commander,
			@RequestParam(value="x",required=true) final float xPosition,
			@RequestParam(value="y",required=true) final float yPosition,
			@RequestParam(value="z",required=true) final float zPosition)
	//@formatter:on
	{
		checkNotNull(name, "missing name");
		final Optional<Avatar> found = this.dao.find(name);
		final AvatarBuilder resultBuilder;
		if (found.isPresent()) {
			resultBuilder = found.get().toBuilder();
		} else {
			resultBuilder = Avatar.builder().name(name);
		}
		resultBuilder.mapId(mapId).worldId(worldId).commander(commander).position(xPosition, yPosition, zPosition);
		return this.dao.update(resultBuilder.build());
	}
}
