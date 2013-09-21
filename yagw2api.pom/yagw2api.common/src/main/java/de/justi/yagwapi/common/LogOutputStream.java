package de.justi.yagwapi.common;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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

import java.io.OutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class logs all bytes written to it as output stream with a specified
 * logging level.
 */
public class LogOutputStream extends OutputStream {
	/** The logger where to log the written bytes. */
	private final Logger logger;

	/** The level. */
	private final Level level;

	/** The internal memory for the written bytes. */
	private StringBuffer buffer;

	/**
	 * Creates a new log output stream which logs bytes to the specified logger
	 * with the specified level.
	 * 
	 * @param logger
	 *            the logger where to log the written bytes
	 * @param level
	 *            the level
	 */
	public LogOutputStream(Logger logger, Level level) {
		this.logger = checkNotNull(logger);
		this.level = checkNotNull(level);
		this.buffer = new StringBuffer();
	}

	/**
	 * @return Returns the logger.
	 */
	public final Logger getLogger() {
		return this.logger;
	}

	/**
	 * @return Returns the logging level.
	 */
	public final Level getLevel() {
		return this.level;
	}

	@Override
	public final void write(int b) {
		byte[] bytes = new byte[1];
		bytes[0] = (byte) (b & 0xff);
		this.buffer = this.buffer.append(new String(bytes));
		if (this.buffer.charAt(this.buffer.length() - 1) == '\n') {
			this.buffer = this.buffer.deleteCharAt(this.buffer.length() - 2);
			this.flush();
		}
	}

	/**
	 * Flushes the output stream.
	 */
	@Override
	public final void flush() {
		final String content = this.buffer.toString();
		if (content.trim().length() > 0) {
			this.logger.log(this.level, this.getClass().getSimpleName() + " >>> " + content.trim());
		}
		this.buffer.delete(0, this.buffer.length() - 1);
	}
}
