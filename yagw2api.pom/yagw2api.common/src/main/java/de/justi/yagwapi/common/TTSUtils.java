package de.justi.yagwapi.common;

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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOError;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.math.DoubleMath;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;

@SuppressWarnings("restriction")
public final class TTSUtils {
	private static final int DEFAULT_PRIORITY = 0;
	private static final double DEFAULT_RATE = 1.2d;
	private static final String MP3_SUFFIX = ".mp3";
	private static final int MAXIMUM_PLAY_TIME_PER_MP3 = 15000;
	private static final int MAX_TEXT_LENGTH_PER_REQUEST = 100;
	private static final Logger LOGGER = LoggerFactory.getLogger(TTSUtils.class);
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) " + "Gecko/20100101 Firefox/11.0";
	private static final long MP3_CACHE_EXPIRE_HOURS = 24;
	private static final Path RELATIVE_TEMP_DIR_PATH = Paths.get("yagw2api", TTSUtils.class.getSimpleName());

	private static final class TTSTask implements Comparable<TTSTask> {
		private final String text;
		private final Locale locale;
		private final int priority;
		private final Date creationTimestamp;
		private final double rate;

		public TTSTask(final String text, final Locale locale, final int priority, final double rate) {
			checkArgument(rate > 0);
			this.text = checkNotNull(text);
			this.locale = checkNotNull(locale);
			this.creationTimestamp = new Date();
			this.priority = priority;
			this.rate = rate;
		}

		/**
		 * @return the text
		 */
		public final String getText() {
			return this.text;
		}

		/**
		 * @return the locale
		 */
		public final Locale getLocale() {
			return this.locale;
		}

		/**
		 * @return the priority
		 */
		public final int getPriority() {
			return this.priority;
		}

		/**
		 * @return the creationTimestamp
		 */
		public final Date getCreationTimestamp() {
			return this.creationTimestamp;
		}

		@Override
		public final int compareTo(final TTSTask o) {
			return DoubleMath
					.roundToInt(
							(Math.signum(new Integer(this.getPriority()).compareTo(o.getPriority())) * -10d)
									+ Math.signum(this.getCreationTimestamp().compareTo(o.getCreationTimestamp())), RoundingMode.FLOOR);
		}

		/**
		 * @return the rate
		 */
		public final double getRate() {
			return this.rate;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("priority", this.getPriority()).add("timestamp", this.getCreationTimestamp()).add("locale", this.getLocale())
					.add("rate", this.getRate()).addValue(this.getText()).toString();
		}
	}

	private static final PriorityBlockingQueue<TTSTask> TASK_QUEUE = Queues.newPriorityBlockingQueue();

	private static volatile boolean initialized = false;

	private static final void initializeIfRequired() {
		if (!initialized) {
			synchronized (TTSUtils.class) {
				if (!initialized) {
					new JFXPanel();
					initialized = true;
				}
			}
		}
	}

	static {

		final Service ttsService = new AbstractScheduledService() {
			@Override
			protected Scheduler scheduler() {
				return Scheduler.newFixedDelaySchedule(0, 100, TimeUnit.MILLISECONDS);
			}

			@Override
			protected void runOneIteration() throws Exception {
				initializeIfRequired();

				final TTSTask task = TASK_QUEUE.poll();

				if (task != null) {
					LOGGER.debug("Going to handle {}", task);
					final List<Path> mp3s = Lists.newArrayList();
					for (String block : divideInTextBlocks(task.getText())) {
						mp3s.add(retrieveMP3File(block, task.getLocale()));
					}
					for (Path mp3 : mp3s) {
						playMP3File(mp3, task.getRate());
					}
					LOGGER.debug("Handled {}", task);
				}
			}
		};
		ttsService.startAsync();
	}

	private TTSUtils() {
	}

	private static final Cache<String, Path> MP3_FILE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(MP3_CACHE_EXPIRE_HOURS, TimeUnit.HOURS)
			.removalListener(new RemovalListener<String, Path>() {
				@Override
				public void onRemoval(final RemovalNotification<String, Path> notification) {
					try {
						java.nio.file.Files.deleteIfExists(notification.getValue());
					} catch (IOException e) {
						throw new IOError(e);
					}
				}
			}).build();

	private static Path retrieveMP3File(final String text, final Locale locale) {
		checkNotNull(text);
		checkArgument(text.trim().length() <= MAX_TEXT_LENGTH_PER_REQUEST, "Text has to be shorter than " + MAX_TEXT_LENGTH_PER_REQUEST + " but actual is " + text.trim().length()
				+ ": \n" + text);
		try {
			final Path mp3 = MP3_FILE_CACHE.get(locale.getLanguage() + text, new Callable<Path>() {
				@Override
				public Path call() throws Exception {
					try {
						Path target = Files.getTempDir().resolve(TTSUtils.RELATIVE_TEMP_DIR_PATH).resolve(DigestUtils.md5Hex(locale.getLanguage() + text) + MP3_SUFFIX);
						if (!java.nio.file.Files.exists(target)) {
							downloadFileFromURI(buildURIForMP3Request(locale, text), target);
						}
						return target;
					} catch (Exception e) {
						LOGGER.error("Failed to retrieve mp3 cached file for text={}", text, e);
						throw e;
					}
				}
			});

			LOGGER.info("Retrieved {} for '{}'", mp3, text);
			return mp3;
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve mp3 cached file for text=" + text, e);
			throw new RuntimeException("Failed to retrieve mp3 cached file for text=" + text, e);
		}
	}

	private static void downloadFileFromURI(final URI uri2download, final Path targetLocation) throws IOException {
		checkNotNull(uri2download);
		checkNotNull(targetLocation);
		// Etablish connection
		final HttpURLConnection connection = (HttpURLConnection) uri2download.toURL().openConnection();
		// Get method
		connection.setRequestMethod("GET");
		// Set User-Agent to "mimic" the behavior of a web browser. In this
		// example, I used my browser's info
		connection.addRequestProperty("User-Agent", USER_AGENT);
		connection.connect();

		java.nio.file.Files.createDirectories(targetLocation.getParent());
		java.nio.file.Files.copy(connection.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	}

	private static void playMP3File(final Path mp3File, final double rate) {
		checkNotNull(mp3File);
		checkArgument(java.nio.file.Files.exists(mp3File), "%s does not exist", mp3File);
		checkArgument(java.nio.file.Files.isRegularFile(mp3File), "%s is no regular file", mp3File);
		checkArgument(rate > 0);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Going to play " + mp3File);
		}
		try {
			final Media mp3Media = new Media(mp3File.toUri().toURL().toString());
			final MediaPlayer mediaPlayer = new MediaPlayer(mp3Media);
			synchronized (mediaPlayer) {
				mediaPlayer.setAutoPlay(false);
				mediaPlayer.setRate(rate);
				mediaPlayer.setOnEndOfMedia(new Runnable() {
					@Override
					public void run() {
						synchronized (mediaPlayer) {
							mediaPlayer.notifyAll();
						}
					}
				});
				mediaPlayer.play();
				try {
					mediaPlayer.wait(MAXIMUM_PLAY_TIME_PER_MP3);
				} catch (InterruptedException e) {
					LOGGER.error("Interrupted while waiting for " + mediaPlayer + " to complete play of " + mp3File);
				}
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(mediaPlayer + " played " + mp3File);
				}
			}

		} catch (MalformedURLException e) {
			throw new RuntimeException("Unable to handle mp3file location: " + mp3File, e);
		}

	}

	private static URI buildURIForMP3Request(final Locale locale, final String text) {
		checkNotNull(locale);
		checkNotNull(text);
		checkArgument(text.trim().length() <= MAX_TEXT_LENGTH_PER_REQUEST, "Text has to be shorter than " + MAX_TEXT_LENGTH_PER_REQUEST + " but actual is " + text.trim().length()
				+ ": \n" + text);
		final URI uri = RuntimeDelegate.getInstance().createUriBuilder().scheme("http").host("translate.google.com").path("translate_tts").queryParam("tl", locale.getLanguage())
				.queryParam("q", text.trim()).build();
		return uri;
	}

	private static List<String> divideInTextBlocks(final String text) {
		StringBuilder resultElement = new StringBuilder();
		final List<String> result = new ArrayList<String>();
		String rest;
		final String[] tokens = text.split(Pattern.compile("(\\s)+").toString());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Tokenized: " + Arrays.deepToString(tokens));
		}
		for (String token : tokens) {
			if ((resultElement.length() + token.length()) < MAX_TEXT_LENGTH_PER_REQUEST) {
				resultElement.append(" ").append(token);
			} else if ((resultElement.length() > 0) && (token.length() < MAX_TEXT_LENGTH_PER_REQUEST)) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("New result element: " + resultElement);
				}
				result.add(resultElement.toString().trim());
				resultElement = new StringBuilder();
				resultElement.append(token);
			} else if (token.length() > 0) {
				if (resultElement.length() > 0) {
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("New result element: " + resultElement);
					}
					result.add(resultElement.toString().trim());
					resultElement = new StringBuilder();
				}
				rest = token;
				do {
					resultElement.append(rest.substring(0, Math.min(rest.length(), MAX_TEXT_LENGTH_PER_REQUEST)));
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Current result element = " + resultElement);
					}
					result.add(resultElement.toString());
					resultElement = new StringBuilder();
					rest = rest.substring(Math.min(rest.length(), MAX_TEXT_LENGTH_PER_REQUEST));
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Current rest = " + rest);
					}
				} while (rest.length() > 0);
				resultElement = new StringBuilder();
			}
		}
		if (resultElement.length() > 0) {
			result.add(resultElement.toString().trim());
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Created text blocks for api calls: " + result);
		}
		return result;
	}

	public static synchronized void readOut(final String rawText, final Locale locale, final Object... arguments) {
		checkNotNull(rawText);
		checkNotNull(locale);
		checkNotNull(arguments);
		final String text = String.format(rawText, arguments);
		readOut(text, locale);
	}

	public static synchronized void readOut(final String text, final Locale locale) {
		checkNotNull(text);
		checkNotNull(locale);
		readOut(text, locale, DEFAULT_PRIORITY, DEFAULT_RATE);
	}

	public static synchronized void readOut(final String rawText, final Locale locale, final int priority, final Object... arguments) {
		checkNotNull(rawText);
		checkNotNull(locale);
		checkNotNull(arguments);
		final String text = String.format(rawText, arguments);
		readOut(text, locale, priority);
	}

	public static synchronized void readOut(final String text, final Locale locale, final int priority) {
		checkNotNull(text);
		checkNotNull(locale);
		readOut(text, locale, priority, DEFAULT_RATE);
	}

	public static synchronized void readOut(final String rawText, final Locale locale, final double rate, final Object... arguments) {
		checkNotNull(rawText);
		checkNotNull(locale);
		checkArgument(rate > 0);
		checkNotNull(arguments);
		final String text = String.format(rawText, arguments);
		readOut(text, locale, rate);
	}

	public static synchronized void readOut(final String text, final Locale locale, final double rate) {
		checkNotNull(text);
		checkNotNull(locale);
		checkArgument(rate > 0);
		readOut(text, locale, DEFAULT_PRIORITY, rate);
	}

	public static synchronized void readOut(final String rawText, final Locale locale, final int priority, final double rate, final Object... arguments) {
		checkNotNull(rawText);
		checkNotNull(locale);
		checkArgument(rate > 0);
		checkNotNull(arguments);
		final String text = String.format(rawText, arguments);
		readOut(text, locale, priority, rate);
	}

	public static synchronized void readOut(final String text, final Locale locale, final int priority, final double rate) {
		checkNotNull(text);
		checkNotNull(locale);
		checkArgument(rate > 0);
		TASK_QUEUE.add(new TTSTask(text, locale, priority, rate));
	}
}
