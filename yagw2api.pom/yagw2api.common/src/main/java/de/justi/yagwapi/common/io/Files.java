package de.justi.yagwapi.common.io;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
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

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.base.Suppliers;

public final class Files {
	private Files() {
		throw new AssertionError("no instance");
	}

	private static final com.google.common.base.Supplier<Path> tempDirSupplier = Suppliers.memoize(new com.google.common.base.Supplier<Path>() {
		@Override
		public Path get() {
			return Paths.get(System.getProperty("java.io.tmpdir"));
		}
	});

	public static Path getTempDir() {
		return tempDirSupplier.get();
	}
}
