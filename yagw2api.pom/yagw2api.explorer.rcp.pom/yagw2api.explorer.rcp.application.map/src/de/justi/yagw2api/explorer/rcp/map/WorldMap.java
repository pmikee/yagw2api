package de.justi.yagw2api.explorer.rcp.map;

import java.nio.file.Path;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.base.Optional;

import de.justi.yagw2api.explorer.rcp.Activator;

public class WorldMap extends ViewPart {

	public static final String ID = "de.justi.yagw2api.explorer.rcp.map.worldmap"; //$NON-NLS-1$

	public WorldMap() {
	}

	/**
	 * Create contents of the view part.
	 *
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		final Canvas canvas = new Canvas(container, SWT.NONE);

		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		canvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(final PaintEvent e) {
				final int sectorSize = 256;
				for (int x = 1; x <= 10; x++) {
					for (int y = 1; y <= 10; y++) {
						final Optional<Path> source = Activator.getDefault().getMapTileService().getMapTile(1, 1, 3, x, y);
						if (source.isPresent()) {
							final Image image = SWTResourceManager.getImage(source.get().toAbsolutePath().toString());
							e.gc.drawImage(image, 0, 0, sectorSize, sectorSize, (x - 1) * sectorSize, (y - 1) * sectorSize, sectorSize, sectorSize);
						}
					}
				}
			}
		});

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();
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
