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
