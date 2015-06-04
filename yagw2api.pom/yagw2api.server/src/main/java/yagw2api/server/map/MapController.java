package yagw2api.server.map;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Server
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

@RestController
@RequestMapping("/map")
public class MapController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MapController.class);

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public ModelAndView get() {
		LOGGER.info("Enter method get");
		final ModelAndView mav = new ModelAndView("map");
		// mav.addObject("mapImage", this.getTile("1", 0, 0, 0, 0));
		LOGGER.info("ModelAndView {}", mav);
		return mav;

	}

	@RequestMapping(value = "/tile/{continentId}/{floorId}/{zoom}/{x}/{y}", method = { RequestMethod.GET })
	public void getTile(final HttpServletResponse response, @PathVariable("continentId") final String continentId, @PathVariable("floorId") final int floorId, @PathVariable("zoom") final int zoom,
			@PathVariable("x") final int x, @PathVariable("y") final int y) throws IOException, URISyntaxException {
		final Optional<Path> optionalPath = YAGW2APIArenanet.INSTANCE.getMapTileService().getMapTile(continentId, floorId, zoom, x, y);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		final Path path = optionalPath.or(Paths.get(ClassLoader.getSystemResource("images/defaultMap.jpg").toURI()));
		Files.copy(path, response.getOutputStream());
	}
}
