package yagw2api.server;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.justi.yagw2api.common.json.TupleTypeAdapter;
import de.justi.yagw2api.common.tuple.Tuple;

@SpringBootApplication
public class ServerMain {

	@Bean
	public Gson gson() {
		return new GsonBuilder().registerTypeHierarchyAdapter(Tuple.class, new TupleTypeAdapter()).setPrettyPrinting().create();
	}

	// STATICS
	public static final void main(final String[] args) {
		@SuppressWarnings("unused")
		final ConfigurableApplicationContext ctx = SpringApplication.run(ServerMain.class);
	}

}
