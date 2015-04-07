package de.justi.yagw2api.explorer.rcp.map;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.annotation.Nullable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.justi.yagw2api.explorer.rcp.Activator;

public final class MapWidget extends Composite {
	// CONSTS
	private static final int SECTOR_SIZE = 256;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapWidget.class);
	private static final ThreadFactory DAEMON_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MAP-UPDATER-DAEMON-%d")
			.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(final Thread t, final Throwable e) {
					LOGGER.error("Uncaught exception in {}", t, e);
				}
			}).build();

	// FIELDS
	private final ExecutorService mapUpdaterService;
	private final Display display;
	@Nullable
	private ScrolledComposite scrolling = null;

	private int continentId = 1;
	private int floor = 1;
	private int zoom = 1;
	private int vSectors = 2;
	private int hSectors = 2;

	// CONSTRUCTOR
	public MapWidget(final Composite parent) {
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(1, false));
		this.scrolling = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		this.scrolling.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		this.scrolling.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.scrolling.setExpandHorizontal(true);
		this.scrolling.setExpandVertical(true);
		this.display = this.getShell().getDisplay();
		this.mapUpdaterService = Executors.newSingleThreadExecutor(DAEMON_THREAD_FACTORY);
	}

	// METHODS
	public void setFloorAndUpdate(final int floor) {
		this.floor = floor;
		this.updateMapAsync();
	}

	public void setZoomAndUpdate(final int zoom) {
		this.zoom = zoom;
		this.updateMapAsync();
	}

	public void setContinentAndUpdate(final int continentId) {
		this.continentId = continentId;
		this.updateMapAsync();
	}

	private synchronized void updateMapAsync() {
		final ScrolledComposite scrolling = this.scrolling;
		final int hSectors = this.hSectors;
		final int vSectors = this.vSectors;
		final int continentId = this.continentId;
		final int floor = this.floor;
		final int zoom = this.zoom;
		this.mapUpdaterService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					final Image cache = new Image(Display.getCurrent(), hSectors * SECTOR_SIZE, vSectors * SECTOR_SIZE);
					final GC gc = new GC(cache);
					for (int x = 0; x <= hSectors; x++) {
						for (int y = 0; y <= vSectors; y++) {
							final Optional<Path> source = Activator.getDefault().getMapTileService().getMapTile(continentId, floor, zoom, x, y);
							if (source.isPresent()) {
								final Image image = SWTResourceManager.getImage(source.get().toAbsolutePath().toString());
								gc.drawImage(image, 0, 0, SECTOR_SIZE, SECTOR_SIZE, x * SECTOR_SIZE, y * SECTOR_SIZE, SECTOR_SIZE, SECTOR_SIZE);
							} else {
								LOGGER.warn("Missing tile for continentId={}, floor={}, zoom={}, x={} and y={}", continentId, floor, zoom, x, y);
							}
						}
					}
					LOGGER.trace("Drawn images to gc={}", gc);
					gc.dispose();
					final Rectangle cacheBounds = cache.getBounds();
					MapWidget.this.display.syncExec(new Runnable() {
						@Override
						public void run() {
							final Canvas mapCanvas = new Canvas(scrolling, SWT.BORDER);
							mapCanvas.addPaintListener(new PaintListener() {

								@Override
								public void paintControl(final PaintEvent e) {
									e.gc.drawImage(cache, 0, 0, cacheBounds.width, cacheBounds.height, 0, 0, cacheBounds.width, cacheBounds.height);
								}
							});
							scrolling.setContent(mapCanvas);
							scrolling.setMinSize(cacheBounds.width, cacheBounds.height);
							scrolling.redraw();
							scrolling.layout();
						}
					});
					LOGGER.trace("Drawn gc to canvas of {}", MapWidget.this.scrolling);
				} catch (Throwable t) {
					LOGGER.error("uncaught exception while updating map for continentId={}, floor={} and zoom={}", continentId, floor, zoom, t);
					throw t;
				}
			}
		});
	}
}
