package de.justi.yagwapi.common.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Queues;
import com.google.common.math.DoubleMath;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;

public final class TTSUtils {
	private static final int DEFAULT_PRIORITY = 0;
	private static final String MP3_SUFFIX = ".mp3";
	private static final int MAXIMUM_PLAY_TIME_PER_MP3 = 15000;
	private static final int MAX_TEXT_LENGTH_PER_REQUEST = 100;
	private static final double RATE = 1.5d;
	private static final Logger LOGGER = Logger.getLogger(TTSUtils.class);
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) " + "Gecko/20100101 Firefox/11.0";
	private static final long MP3_CACHE_EXPIRE_HOURS = 24;
	private static final File YAGW2API_TEMP_FILE;

	private static final class TTSTask implements Comparable<TTSTask> {
		private final String text;
		private final Locale locale;
		private final int priority;
		private final Date creationTimestamp;

		public TTSTask(String text, Locale locale, int priority) {
			this.text = checkNotNull(text);
			this.locale = checkNotNull(locale);
			this.creationTimestamp = new Date();
			this.priority = priority;
		}

		/**
		 * @return the text
		 */
		public final String getText() {
			return text;
		}

		/**
		 * @return the locale
		 */
		public final Locale getLocale() {
			return locale;
		}

		/**
		 * @return the priority
		 */
		public final int getPriority() {
			return priority;
		}

		/**
		 * @return the creationTimestamp
		 */
		public final Date getCreationTimestamp() {
			return creationTimestamp;
		}

		@Override
		public int compareTo(TTSTask o) {
			return DoubleMath.roundToInt(
					(Math.signum(new Integer(this.getPriority()).compareTo(o.getPriority())) * 10d) + Math.signum(this.getCreationTimestamp().compareTo(o.getCreationTimestamp())), RoundingMode.FLOOR);
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("priority", this.getPriority()).add("timestamp", this.getCreationTimestamp()).add("locale", this.getLocale()).addValue(this.getText()).toString();
		}
	}

	private static final Queue<TTSTask> TASK_QUEUE = Queues.newPriorityBlockingQueue();

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
		try {
			final File tempDirDetectionFile = File.createTempFile(UUID.randomUUID().toString(), ".dat");
			checkState(tempDirDetectionFile.delete());
			YAGW2API_TEMP_FILE = new File(tempDirDetectionFile.getParentFile(), "yagw2api");
			checkState((YAGW2API_TEMP_FILE.exists() && YAGW2API_TEMP_FILE.isDirectory()) || YAGW2API_TEMP_FILE.mkdirs());
		} catch (IOException e) {
			LOGGER.fatal("Failed to initialize " + TTSUtils.class.getSimpleName(), e);
			throw new RuntimeException("Failed to initialize " + TTSUtils.class.getSimpleName(), e);
		}

		final Service ttsService = new AbstractScheduledService() {
			@Override
			protected Scheduler scheduler() {
				return Scheduler.newFixedDelaySchedule(0, 100, TimeUnit.MILLISECONDS);
			}

			@Override
			protected void runOneIteration() throws Exception {
				initializeIfRequired();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Queue: " + TASK_QUEUE);
				}
				final TTSTask task = TASK_QUEUE.poll();

				if (task != null) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Going to handle " + task);
					}
					final List<File> mp3s = new ArrayList<File>();
					for (String block : divideInTextBlocks(task.getText())) {
						mp3s.add(retrieveMP3File(block, task.getLocale()));
					}
					for (File mp3 : mp3s) {
						playMP3File(mp3);
					}
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Handled " + task);
					}
				}
			}
		};
		ttsService.startAndWait();
	}

	private TTSUtils() {
	}

	private static final Cache<String, File> MP3_FILE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(MP3_CACHE_EXPIRE_HOURS, TimeUnit.HOURS).removalListener(new RemovalListener<String, File>() {
		@Override
		public void onRemoval(RemovalNotification<String, File> notification) {
			if (notification.getValue().exists()) {
				notification.getValue().delete();
			}
		}
	}).build();

	private static File retrieveMP3File(final String text, final Locale locale) {
		checkNotNull(text);
		checkArgument(text.trim().length() <= MAX_TEXT_LENGTH_PER_REQUEST, "Text has to be shorter than " + MAX_TEXT_LENGTH_PER_REQUEST + " but actual is " + text.trim().length() + ": \n" + text);
		try {
			final File mp3 = MP3_FILE_CACHE.get(locale.getLanguage() + text, new Callable<File>() {
				@Override
				public File call() throws Exception {
					try {
						final File target = new File(YAGW2API_TEMP_FILE, DigestUtils.md5Hex(locale.getLanguage() + text) + MP3_SUFFIX);
						if (!target.exists()) {
							downloadFileFromURI(buildURIForMP3Request(locale, text), target);
						}
						checkState(target.isFile());
						return target;
					} catch (Exception e) {
						LOGGER.error("Failed to retrieve mp3 cached file for text=" + text, e);
						throw e;
					}
				}
			});

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Retrieved " + mp3 + " for '" + text + "'.");
			}
			return mp3;
		} catch (ExecutionException e) {
			LOGGER.error("Failed to retrieve mp3 cached file for text=" + text, e);
			throw new RuntimeException("Failed to retrieve mp3 cached file for text=" + text, e);
		}
	}

	private static void downloadFileFromURI(URI uri2download, File targetLocation) throws IOException {
		checkNotNull(uri2download);
		checkNotNull(targetLocation);
		checkArgument(targetLocation.exists() == false);
		// Etablish connection
		final HttpURLConnection connection = (HttpURLConnection) uri2download.toURL().openConnection();
		// Get method
		connection.setRequestMethod("GET");
		// Set User-Agent to "mimic" the behavior of a web browser. In this
		// example, I used my browser's info
		connection.addRequestProperty("User-Agent", USER_AGENT);
		connection.connect();

		final BufferedInputStream bufIn = new BufferedInputStream(connection.getInputStream());
		byte[] buffer = new byte[1024];
		int n;
		ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
		while ((n = bufIn.read(buffer)) > 0) {
			bufOut.write(buffer, 0, n);
		}

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetLocation));
		out.write(bufOut.toByteArray());
		out.flush();
		out.close();
	}

	private static void playMP3File(final File mp3File) {
		checkNotNull(mp3File);
		checkArgument(mp3File.exists());
		checkArgument(mp3File.isFile());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Going to play " + mp3File);
		}
		try {
			final Media mp3Media = new Media(mp3File.toURI().toURL().toString());
			final MediaPlayer mediaPlayer = new MediaPlayer(mp3Media);
			synchronized (mediaPlayer) {
				mediaPlayer.setAutoPlay(false);
				mediaPlayer.setRate(RATE);
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
			LOGGER.fatal("Unable to handle mp3file location: " + mp3File);
			throw new RuntimeException("Unable to handle mp3file location: " + mp3File, e);
		}

	}

	private static URI buildURIForMP3Request(final Locale locale, final String text) {
		checkNotNull(locale);
		checkNotNull(text);
		checkArgument(text.trim().length() <= MAX_TEXT_LENGTH_PER_REQUEST, "Text has to be shorter than " + MAX_TEXT_LENGTH_PER_REQUEST + " but actual is " + text.trim().length() + ": \n" + text);
		final URI uri = RuntimeDelegate.getInstance().createUriBuilder().scheme("http").host("translate.google.com").path("translate_tts").queryParam("tl", locale.getLanguage())
				.queryParam("q", text.trim()).build();
		return uri;
	}

	private static List<String> divideInTextBlocks(String text) {
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

	public static synchronized void readOut(final String text, final Locale locale) {
		checkNotNull(text);
		checkNotNull(locale);
		readOut(text, locale, DEFAULT_PRIORITY);
	}

	public static synchronized void readOut(final String text, final Locale locale, int priority) {
		checkNotNull(text);
		checkNotNull(locale);
		TASK_QUEUE.add(new TTSTask(text, locale, priority));
	}
}
