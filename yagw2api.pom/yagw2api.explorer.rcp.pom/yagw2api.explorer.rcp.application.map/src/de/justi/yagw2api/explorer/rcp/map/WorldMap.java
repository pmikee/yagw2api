package de.justi.yagw2api.explorer.rcp.map;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.justi.yagw2api.explorer.rcp.Activator;

public class WorldMap extends ViewPart {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldMap.class);
	private static final int SECTOR_SIZE = 256;
	public static final String ID = "de.justi.yagw2api.explorer.rcp.map.worldmap"; //$NON-NLS-1$
	public static final ThreadFactory DAEMON_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MAP-UPDATER-DAEMON-%d")
			.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(final Thread t, final Throwable e) {
					LOGGER.error("Uncaught exception in {}", t, e);
				}
			}).build();
	private ScrolledComposite scrolling = null;
	private final ExecutorService mapUpdaterService;

	public WorldMap() {
		this.mapUpdaterService = Executors.newSingleThreadExecutor(DAEMON_THREAD_FACTORY);
	}

	private void initializeMapAsync(final int continentId, final int floor, final int zoom, final int vSectors, final int hSectors) {
		this.mapUpdaterService.submit(new Runnable() {

			@Override
			public void run() {
				final Image cache = new Image(Display.getCurrent(), hSectors * SECTOR_SIZE, vSectors * SECTOR_SIZE);
				final GC gc = new GC(cache);
				for (int x = 0; x <= hSectors; x++) {
					for (int y = 0; y <= vSectors; y++) {
						final Optional<Path> source = Activator.getDefault().getMapTileService().getMapTile(continentId, floor, zoom, x, y);
						if (source.isPresent()) {
							final Image image = SWTResourceManager.getImage(source.get().toAbsolutePath().toString());
							gc.drawImage(image, 0, 0, SECTOR_SIZE, SECTOR_SIZE, x * SECTOR_SIZE, y * SECTOR_SIZE, SECTOR_SIZE, SECTOR_SIZE);
						}
					}
				}
				gc.dispose();
				final Rectangle cacheBounds = cache.getBounds();
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						final Canvas mapCanvas = new Canvas(WorldMap.this.scrolling, SWT.BORDER);
						mapCanvas.addPaintListener(new PaintListener() {

							@Override
							public void paintControl(final PaintEvent e) {
								e.gc.drawImage(cache, 0, 0, cacheBounds.width, cacheBounds.height, 0, 0, cacheBounds.width, cacheBounds.height);
							}
						});
						WorldMap.this.scrolling.setContent(mapCanvas);
						WorldMap.this.scrolling.setMinSize(cacheBounds.width, cacheBounds.height);
						WorldMap.this.scrolling.redraw();
						WorldMap.this.scrolling.layout();
					}
				});
			}
		});
	}

	/**
	 * Create contents of the view part.
	 *
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {

		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		this.scrolling = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		this.scrolling.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		this.scrolling.setExpandHorizontal(true);
		this.scrolling.setExpandVertical(true);

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();
		this.initializeMapAsync(1, 1, 3, 5, 5);
		this.initializeMapAsync(1, 1, 4, 5, 5);
		this.initializeMapAsync(1, 1, 5, 5, 5);
		this.initializeMapAsync(1, 1, 6, 5, 5);
		this.initializeMapAsync(1, 1, 7, 5, 5);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = this.getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = this.getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
