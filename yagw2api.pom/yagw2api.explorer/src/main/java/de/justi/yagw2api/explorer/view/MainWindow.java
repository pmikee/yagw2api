package de.justi.yagw2api.explorer.view;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.DockedTypeDescriptor;
import org.noos.xing.mydoggy.PersistenceDelegate.MergePolicy;
import org.noos.xing.mydoggy.PushAwayMode;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.ToolWindowGroup;
import org.noos.xing.mydoggy.ToolWindowManagerDescriptor;
import org.noos.xing.mydoggy.ToolWindowType;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.noos.xing.mydoggy.plaf.ui.DockableDescriptor;

import com.google.common.base.Optional;
import com.google.common.io.Closer;
import com.google.common.math.DoubleMath;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.explorer.model.APIStatusTableModel;
import de.justi.yagw2api.explorer.model.MapObjectivesTableModel;
import de.justi.yagw2api.explorer.model.MatchDetailsTableModel;
import de.justi.yagw2api.explorer.model.MatchesTableModel;
import de.justi.yagw2api.explorer.renderer.MatchDetailsTableCellRenderer;
import de.justi.yagw2api.explorer.renderer.ObjectiveTableCellRenderer;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveUnclaimedEvent;
import de.justi.yagwapi.common.utils.TTSUtils;

public final class MainWindow extends AbstractWindow implements IWVWMapListener {
	private static final String WORKSPACE_XML_FILENAME = "workspace.xml";
	private static final long serialVersionUID = -6500541020042114865L;
	private static final Logger LOGGER = Logger.getLogger(MainWindow.class);
	public static final Color ETERNAL_BATTLEGROUNDS_FG = new Color(200, 130, 0);
	public static final Color ETERNAL_BATTLEGROUNDS_BG = new Color(200, 130, 0, 100);
	public static final Color GREEN_WORLD_FG = new Color(70, 152, 42);
	public static final Color GREEN_WORLD_BG = new Color(70, 152, 42, 100);
	public static final Color BLUE_WORLD_FG = new Color(35, 129, 199);
	public static final Color BLUE_WORLD_BG = new Color(35, 129, 199, 100);
	public static final Color RED_WORLD_FG = new Color(175, 25, 10);
	public static final Color RED_WORLD_BG = new Color(175, 25, 10, 100);

	private final JTable matchesTable;
	private final MatchesTableModel matchesTableModel;
	private Optional<IWVWMatch> selectedMatch = Optional.absent();
	private Optional<IWVWWrapper> wrapper = Optional.absent();

	private final JTable eternalTable;
	private final MapObjectivesTableModel eternalMapModel;

	private final JTable matchDetailslTable;
	private final MatchDetailsTableModel matchDetailsTableModel;

	private final JTable allMapsTable;
	private final MapObjectivesTableModel allMapsModel;

	private final JTable greenTable;
	private final MapObjectivesTableModel greenMapModel;
	private final JTable blueTable;
	private final MapObjectivesTableModel blueMapModel;
	private final JTable redTable;
	private final MapObjectivesTableModel redMapModel;
	private final ToolWindow matchesToolWindow;
	private final ToolWindow allMapsToolWindow;
	private final ToolWindow eternalMapToolWindow;
	private final ToolWindow blueMapToolWindow;
	private final ToolWindow greenMapToolWindow;
	private final ToolWindow redMapToolWindow;
	private final ToolWindow matchDetailsToolWindow;
	private final MyDoggyToolWindowManager toolWindowManager;
	private ToolWindowGroup singleMapTableToolWindows;

	private final JTable apiStatusTable;
	private final APIStatusTableModel apiStatusTableModel;
	private ToolWindow apiStatusToolWindow;

	private static final class GW2TileFactoryInfo extends TileFactoryInfo {
		private static final int MAX_ZOOM = 6;
		private static final int MIN_ZOOM = 0;

		public GW2TileFactoryInfo() {
			super(MIN_ZOOM, MAX_ZOOM - 1, MAX_ZOOM, 256, false, true, "https://tiles.guildwars2.com/2/3", "", "", "");
		}

		@Override
		public int getTileSize(int zoom) {
			return 256;
		}

		@Override
		public int getDefaultZoomLevel() {
			return MIN_ZOOM;
		}

		@Override
		public String getTileUrl(int x, int y, int zoom) {
			final int x2use = x;
			final int y2use = y;
			final int zoom2use = MAX_ZOOM - zoom;
			final String url = this.baseURL + "/" + zoom2use + "/" + x2use + "/" + y2use + ".jpg";
			return url;
		}

		@Override
		public int getMapWidthInTilesAtZoom(int zoom) {
			return DoubleMath.roundToInt(Math.pow(2, MAX_ZOOM - zoom), RoundingMode.UP);
		}

		/**
		 * 
		 * @param zoom
		 * @return
		 */
		@Override
		public double getLongitudeDegreeWidthInPixels(int zoom) {
			return this.getTileSize(zoom) / 360d;
		}

		/**
		 * 
		 * @param zoom
		 * @return
		 */
		@Override
		public double getLongitudeRadianWidthInPixels(int zoom) {
			return this.getTileSize(zoom) / (2d * Math.PI);
		}
	}

	public MainWindow() {
		super();
		this.setTitle("Yet Another GW2 API - Explorer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(640, 480));
		this.setPreferredSize(new Dimension(1024, 768));

		this.matchesTableModel = new MatchesTableModel();
		this.eternalMapModel = new MapObjectivesTableModel();
		this.greenMapModel = new MapObjectivesTableModel();
		this.blueMapModel = new MapObjectivesTableModel();
		this.redMapModel = new MapObjectivesTableModel();
		this.allMapsModel = new MapObjectivesTableModel();
		this.matchDetailsTableModel = new MatchDetailsTableModel();
		this.apiStatusTableModel = new APIStatusTableModel();

		this.getContentPanel().add(this.builtMainMenuBar(), BorderLayout.NORTH);

		this.toolWindowManager = new MyDoggyToolWindowManager();
		this.getContentPanel().add(this.toolWindowManager, BorderLayout.CENTER);
		final ToolWindowManagerDescriptor toolWindowManagerDesc = this.toolWindowManager.getToolWindowManagerDescriptor();
		toolWindowManagerDesc.setNumberingEnabled(false);
		toolWindowManagerDesc.setPushAwayMode(PushAwayMode.MOST_RECENT);

		this.toolWindowManager.resetMainContent();
		final ContentManager contentManager = this.toolWindowManager.getContentManager();
		contentManager.addContent("Worldmap", "Worldmap", null, this.buildMap(), "Worldmap");

		this.singleMapTableToolWindows = this.toolWindowManager.getToolWindowGroup("mapTableToolWindows");

		this.allMapsTable = this.initMapTable(this.allMapsModel);
		this.allMapsToolWindow = this.toolWindowManager.registerToolWindow("All Maps", "All Maps", null, new JScrollPane(this.allMapsTable), ToolWindowAnchor.LEFT);
		this.allMapsToolWindow.setVisible(true);
		final DockedTypeDescriptor allMapsToolWindowDescriptor = (DockedTypeDescriptor) this.allMapsToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		allMapsToolWindowDescriptor.setIdVisibleOnTitleBar(false);
		allMapsToolWindowDescriptor.setTitleBarButtonsVisible(false);
		allMapsToolWindowDescriptor.setTitleBarVisible(false);

		this.eternalTable = this.initMapTable(this.eternalMapModel);
		this.eternalMapToolWindow = this.toolWindowManager.registerToolWindow("Enternal Battlegrounds", "Enternal Battlegrounds", null, new JScrollPane(this.eternalTable), ToolWindowAnchor.RIGHT);
		this.eternalMapToolWindow.setAggregateMode(true);
		this.singleMapTableToolWindows.addToolWindow(this.eternalMapToolWindow);
		final DockedTypeDescriptor eternalMapToolWindowDescriptor = (DockedTypeDescriptor) this.eternalMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		eternalMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.blueTable = this.initMapTable(this.blueMapModel);
		this.blueMapToolWindow = this.toolWindowManager.registerToolWindow("Blue Borderlands", "Blue Borderlands", null, new JScrollPane(this.blueTable), ToolWindowAnchor.RIGHT);
		this.blueMapToolWindow.setAggregateMode(true);
		this.singleMapTableToolWindows.addToolWindow(this.blueMapToolWindow);
		final DockedTypeDescriptor blueMapToolWindowDescriptor = (DockedTypeDescriptor) this.blueMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		blueMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.greenTable = this.initMapTable(this.greenMapModel);
		this.greenMapToolWindow = this.toolWindowManager.registerToolWindow("Green Borderlands", "Green Borderlands", null, new JScrollPane(this.greenTable), ToolWindowAnchor.RIGHT);
		this.greenMapToolWindow.setAggregateMode(true);
		this.singleMapTableToolWindows.addToolWindow(this.greenMapToolWindow);
		final DockedTypeDescriptor greenMapToolWindowDescriptor = (DockedTypeDescriptor) this.greenMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		greenMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.redTable = this.initMapTable(this.redMapModel);
		this.redMapToolWindow = this.toolWindowManager.registerToolWindow("Red Borderlands", "Red Borderlands", null, new JScrollPane(this.redTable), ToolWindowAnchor.RIGHT);
		this.redMapToolWindow.setAggregateMode(true);
		this.singleMapTableToolWindows.addToolWindow(this.redMapToolWindow);
		final DockedTypeDescriptor redMapToolWindowDescriptor = (DockedTypeDescriptor) this.redMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		redMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.singleMapTableToolWindows.setImplicit(false);
		this.singleMapTableToolWindows.setVisible(true);

		this.apiStatusTable = this.initAPIStatusTable(this.apiStatusTableModel);
		this.apiStatusToolWindow = this.toolWindowManager.registerToolWindow("API Status", "API Status", null, new JScrollPane(this.apiStatusTable), ToolWindowAnchor.BOTTOM);
		this.apiStatusToolWindow.setVisible(true);
		this.apiStatusToolWindow.setAggregateMode(true);
		final DockedTypeDescriptor apiStatusToolWindowDescriptor = (DockedTypeDescriptor) this.apiStatusToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		apiStatusToolWindowDescriptor.setIdVisibleOnTitleBar(false);
		apiStatusToolWindowDescriptor.setTitleBarButtonsVisible(false);
		apiStatusToolWindowDescriptor.setTitleBarVisible(false);

		this.matchDetailslTable = this.initMatchDetailsTable(this.matchDetailsTableModel);
		this.matchDetailsToolWindow = this.toolWindowManager.registerToolWindow("Match Details", "Match Details", null, new JScrollPane(this.matchDetailslTable), ToolWindowAnchor.BOTTOM);
		this.matchDetailsToolWindow.setVisible(true);
		this.matchDetailsToolWindow.setAggregateMode(true);
		final DockedTypeDescriptor matchDetailsToolWindowDescriptor = (DockedTypeDescriptor) this.matchDetailsToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		matchDetailsToolWindowDescriptor.setIdVisibleOnTitleBar(false);
		matchDetailsToolWindowDescriptor.setTitleBarButtonsVisible(false);
		matchDetailsToolWindowDescriptor.setTitleBarVisible(false);

		this.matchesTable = this.initMatchesTable(this.matchesTableModel);
		this.matchesToolWindow = this.toolWindowManager.registerToolWindow("Matches Overview", "Matches Overview", null, new JScrollPane(this.matchesTable), ToolWindowAnchor.BOTTOM);
		this.matchesToolWindow.setVisible(true);
		this.matchesToolWindow.setAggregateMode(true);
		final DockedTypeDescriptor matchesToolWindowDescriptor = (DockedTypeDescriptor) this.matchesToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		matchesToolWindowDescriptor.setIdVisibleOnTitleBar(false);
		matchesToolWindowDescriptor.setTitleBarButtonsVisible(false);
		matchesToolWindowDescriptor.setTitleBarVisible(false);

		final DockableDescriptor memoryMonitorDescriptor = new MemoryMonitorDockableDescriptor(this.toolWindowManager, ToolWindowAnchor.BOTTOM, false);
		memoryMonitorDescriptor.setAvailable(true);
		memoryMonitorDescriptor.setAnchor(ToolWindowAnchor.BOTTOM, 0);

		//
		// final ToolWindow chartTestToolWindow =
		// this.toolWindowManager.registerToolWindow("Chart Test", "Chart Test",
		// null, this.buildChart(), ToolWindowAnchor.TOP);
		// final DockedTypeDescriptor chartTestToolWindowDescriptor =
		// (DockedTypeDescriptor)
		// chartTestToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		// chartTestToolWindowDescriptor.setIdVisibleOnTitleBar(false);
		// chartTestToolWindow.setVisible(true);

		this.pack();

		// finally try to restore workspace setting
		this.loadWorkspaceSettings();
	}

	private JTable initAPIStatusTable(final APIStatusTableModel tableModel) {
		final String[] header = { "API", "State", "Description", "Ping", "Retrieve", "Record", "Time" };
		final JTable table = new JTable(tableModel, newTCM(header));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final TableRowSorter<APIStatusTableModel> sorter = new TableRowSorter<APIStatusTableModel>(tableModel);
		table.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for (int col = 0; col < tableModel.getColumnCount(); col++) {
			sorter.setComparator(col, tableModel.getColumnComparator(col));
		}
		return table;
	}

	// private ChartPanel buildChart() {
	// final TimeSeriesCollection dataset = new TimeSeriesCollection();
	// TimeSeries series;
	// for (IWorldEntity world :
	// YAGW2APIAnalyzer.getWorldEntityDAO().retrieveAllWorldEntities()) {
	// for (IWVWMatchEntity match : world.getParticipatedInMatches()) {
	// series = new TimeSeries(world.getName().get() + ": " +
	// match.getStartTimestamp() + "-" + match.getEndTimestamp(),
	// Millisecond.class);
	// int i;
	// if (match.getBlueWorld().equals(world)) {
	// i = 1;
	// } else if (match.getGreenWorld().equals(world)) {
	// i = 2;
	// } else {
	// i = 3;
	// }
	//
	// Map<Date, IWVWScoresEmbeddable> scores = match.getScores();
	// for (Date key : scores.keySet()) {
	// if (i == 1) {
	// series.add(new Millisecond(key), scores.get(key).getBlueScore());
	// } else if (i == 2) {
	// series.add(new Millisecond(key), scores.get(key).getGreenScore());
	// } else {
	// series.add(new Millisecond(key), scores.get(key).getRedScore());
	// }
	// }
	//
	// dataset.addSeries(series);
	// }
	// }
	//
	// final JFreeChart chart =
	// ChartFactory.createTimeSeriesChart("Legal & General Unit Trust Prices",
	// // title
	// "Date", // x-axis label
	// "Total Points", // y-axis label
	// dataset, // data
	// false, // create legend?
	// true, // generate tooltips?
	// false);
	//
	// chart.setBackgroundPaint(Color.white);
	//
	// XYPlot plot = (XYPlot) chart.getPlot();
	// plot.setBackgroundPaint(Color.lightGray);
	// plot.setDomainGridlinePaint(Color.white);
	// plot.setRangeGridlinePaint(Color.white);
	// plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
	// plot.setDomainCrosshairVisible(true);
	// plot.setRangeCrosshairVisible(true);
	//
	// final ChartPanel chartPanel = new ChartPanel(chart);
	// return chartPanel;
	//
	// }

	private boolean loadWorkspaceSettings() {
		boolean success;
		Closer closer = Closer.create();
		try {
			try {
				final FileInputStream loadSource = closer.register(new FileInputStream(WORKSPACE_XML_FILENAME));
				MainWindow.this.toolWindowManager.getPersistenceDelegate().merge(loadSource, MergePolicy.RESET);
				LOGGER.info("Successfull loaded workspace settings.");
				success = true;
			} catch (Throwable ex) {
				success = false;
				closer.rethrow(ex);
			} finally {
				closer.close();
			}
		} catch (IOException ex) {
			LOGGER.error("Exeption thrown while loading workspace settings.", ex);
			success = false;
		}
		return success;
	}

	private boolean saveWorskapceSettings() {
		boolean success;
		Closer closer = Closer.create();
		try {
			try {
				final FileOutputStream saveDestination = closer.register(new FileOutputStream(WORKSPACE_XML_FILENAME));
				MainWindow.this.toolWindowManager.getPersistenceDelegate().save(saveDestination);
				LOGGER.info("Successfull saved workspace settings.");
				success = true;
			} catch (Throwable ex) {
				closer.rethrow(ex);
				success = false;
			} finally {
				closer.close();
			}
		} catch (IOException ex) {
			LOGGER.error("Exeption thrown while saving workspace settings.", ex);
			success = false;
		}
		return success;
	}

	private JMenuBar builtMainMenuBar() {
		final JMenuBar mainMenuBar = new JMenuBar();
		final JMenu windowMenu = new JMenu("Window");
		final JMenuItem savePerspectiveMenuItem = new JMenuItem("Save Perspective");
		savePerspectiveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.this.saveWorskapceSettings();
			}
		});
		windowMenu.add(savePerspectiveMenuItem);
		final JMenuItem loadPerspectiveMenuItem = new JMenuItem("Load Perspective");
		loadPerspectiveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.this.loadWorkspaceSettings();
			}
		});
		windowMenu.add(loadPerspectiveMenuItem);
		mainMenuBar.add(windowMenu);
		final JMenu settingsMenu = new JMenu("Settings");
		final JMenu settingLocaleMenu = new JMenu("Language");
		final JMenuItem enLocaleMenuItem = new JMenuItem("en");
		enLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIWrapper.setCurrentLocale(Locale.ENGLISH);
			}
		});
		settingLocaleMenu.add(enLocaleMenuItem);
		final JMenuItem deLocaleMenuItem = new JMenuItem("de");
		deLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIWrapper.setCurrentLocale(Locale.GERMANY);
			}
		});
		settingLocaleMenu.add(deLocaleMenuItem);
		final JMenuItem frLocaleMenuItem = new JMenuItem("fr");
		frLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIWrapper.setCurrentLocale(Locale.FRANCE);
			}
		});
		settingLocaleMenu.add(frLocaleMenuItem);
		final JMenuItem esLocaleMenuItem = new JMenuItem("es");
		esLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIWrapper.setCurrentLocale(Locale.forLanguageTag("es"));
			}
		});
		settingLocaleMenu.add(esLocaleMenuItem);
		settingsMenu.add(settingLocaleMenu);
		mainMenuBar.add(settingsMenu);
		return mainMenuBar;
	}

	private JXMapKit buildMap() {
		final JXMapKit mapkit = new JXMapKit();
		mapkit.setDefaultProvider(DefaultProviders.Custom);
		mapkit.setAddressLocationShown(false);
		final TileFactory factory = new DefaultTileFactory(new GW2TileFactoryInfo());
		mapkit.setTileFactory(factory);
		mapkit.setMiniMapVisible(false);
		mapkit.setPreferredSize(new Dimension(640, 480));

		mapkit.getMainMap().setDrawTileBorders(true);
		mapkit.getMainMap().setRestrictOutsidePanning(true);
		mapkit.getMainMap().setHorizontalWrapped(false);
		mapkit.getMainMap().setRecenterOnClickEnabled(true);
		return mapkit;
	}

	private TableColumnModel newTCM(String[] header) {

		final TableColumnModel tcm = new DefaultTableColumnModel();
		List<TableColumn> tcl = new ArrayList<>();
		for (int i = 0; i < header.length; i++) {
			tcl.add(new TableColumn(i));
			tcl.get(i).setHeaderValue(header[i]);
			tcm.addColumn(tcl.get(i));
		}

		return tcm;
	}

	public void wireUp(IWVWWrapper wrapper) {
		checkNotNull(wrapper);
		checkState(this.matchesTableModel != null);
		if (this.wrapper.isPresent()) {
			this.wrapper.get().unregisterWVWMatchListener(this.matchesTableModel);
		}
		this.wrapper = Optional.of(wrapper);
		wrapper.registerWVWMatchListener(this.matchesTableModel);
	}

	private JTable initMatchesTable(final MatchesTableModel tableModel) {
		final String[] header = { "ID", "Region", "Grün", "Punkte (Grün)", "Blau", "Punkte (Blau)", "Rot", "Punkte (Rot)", "Start", "Ende" };
		final JTable matchesTable = new JTable(this.matchesTableModel, newTCM(header));
		matchesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final TableRowSorter<MatchesTableModel> sorter = new TableRowSorter<MatchesTableModel>(this.matchesTableModel);
		matchesTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for (int col = 0; col < tableModel.getColumnCount(); col++) {
			sorter.setComparator(col, tableModel.getColumnComparator(col));
		}

		matchesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						final int index = matchesTable.convertRowIndexToModel(matchesTable.getSelectedRow());
						final Optional<IWVWMatch> matchOptional = MainWindow.this.matchesTableModel.getMatch(index);
						if (matchOptional.isPresent()) {
							LOGGER.debug("Incoming selection event [" + index + "] -> match=" + matchOptional.get().getId());
							if (!selectedMatch.equals(matchOptional)) {
								selectedMatch = matchOptional;
								final IWVWMatch match = matchOptional.get();
								LOGGER.info("NEW selected match=" + match.getId());
								if (wrapper.isPresent()) {
									MainWindow.this.allMapsModel.wireUp(MainWindow.this.wrapper.get(), match.getCenterMap(), match.getGreenMap(), match.getBlueMap(), match.getRedMap());
									MainWindow.this.matchDetailsTableModel.wireUp(MainWindow.this.wrapper.get(), match, match.getCenterMap(), match.getGreenMap(), match.getBlueMap(),
											match.getRedMap());
									MainWindow.this.eternalMapModel.wireUp(MainWindow.this.wrapper.get(), match.getCenterMap());
									MainWindow.this.greenMapModel.wireUp(MainWindow.this.wrapper.get(), match.getGreenMap());
									MainWindow.this.blueMapModel.wireUp(MainWindow.this.wrapper.get(), match.getBlueMap());
									MainWindow.this.redMapModel.wireUp(MainWindow.this.wrapper.get(), match.getRedMap());

									MainWindow.this.wrapper.get().unregisterWVWMapListener(MainWindow.this);
									MainWindow.this.wrapper.get().registerWVWMapListener(match.getCenterMap(), MainWindow.this);
									MainWindow.this.wrapper.get().registerWVWMapListener(match.getGreenMap(), MainWindow.this);
									MainWindow.this.wrapper.get().registerWVWMapListener(match.getBlueMap(), MainWindow.this);
									MainWindow.this.wrapper.get().registerWVWMapListener(match.getRedMap(), MainWindow.this);
								}

								if (match.getGreenWorld().getName().isPresent()) {
									MainWindow.this.greenMapToolWindow.setTitle(match.getGreenWorld().getName().get() + " Borderlands");
								} else {
									MainWindow.this.greenMapToolWindow.setTitle("Green Borderlands");
								}
								if (match.getBlueWorld().getName().isPresent()) {
									MainWindow.this.blueMapToolWindow.setTitle(match.getBlueWorld().getName().get() + " Borderlands");
								} else {
									MainWindow.this.blueMapToolWindow.setTitle("Blue Borderlands");
								}
								if (match.getRedWorld().getName().isPresent()) {
									MainWindow.this.redMapToolWindow.setTitle(match.getRedWorld().getName().get() + " Borderlands");
								} else {
									MainWindow.this.redMapToolWindow.setTitle("Red Borderlands");
								}
								LOGGER.debug("Wired everything up for new selected match=" + match.getId());
							} else {
								LOGGER.debug("Already selected match=" + matchOptional.get().getId());
							}
						}
					}
				});

			}
		});
		return matchesTable;
	}

	private JTable initMatchDetailsTable(MatchDetailsTableModel tableModel) {
		checkNotNull(tableModel);
		final String[] header = { "", "Punkte (Grün)", "Punktezuwachs (Grün)", "Punkte (Blau)", "Punktezuwachs (Blau)", "Punkte (Rot)", "Punktezuwachs (Rot)" };
		final TableColumnModel tcm = this.newTCM(header);
		final JTable matchDetailsTable = new JTable(tableModel, tcm);
		matchDetailsTable.setDefaultRenderer(Object.class, new MatchDetailsTableCellRenderer());
		matchDetailsTable.getTableHeader().setReorderingAllowed(false);
		matchDetailsTable.setEnabled(false);

		return matchDetailsTable;
	}

	private JTable initMapTable(MapObjectivesTableModel tableModel) {
		checkNotNull(tableModel);
		final String[] header = { "Karte", "Objekt", "Objekttyp", "Wert", "Besitzer", "Buffende", "Verbleibender Buff", "Gilde", "Gildentag" };
		final JTable mapTable = new JTable(tableModel, this.newTCM(header));
		mapTable.setDefaultRenderer(Object.class, new ObjectiveTableCellRenderer());
		final TableRowSorter<MapObjectivesTableModel> sorter = new TableRowSorter<MapObjectivesTableModel>(tableModel);
		mapTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for (int col = 0; col < tableModel.getColumnCount(); col++) {
			sorter.setComparator(col, tableModel.getColumnComparator(col));
		}
		return mapTable;
	}

	@Override
	public void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		TTSUtils.readOut(event.getObjective().getLabel().get() + " wurde von " + event.getNewOwningWorld().getName().get() + " erobert.", YAGW2APIArenanet.getInstance().getCurrentLocale());
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		TTSUtils.readOut(event.getObjective().getLabel().get() + " hat keinen Buff mehr.", YAGW2APIArenanet.getInstance().getCurrentLocale());
	}

	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
		TTSUtils.readOut(event.getObjective().getLabel().get() + " wurde von " + event.getClaimingGuild().getName() + " eingenommen.", YAGW2APIArenanet.getInstance().getCurrentLocale());
	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
	}

	@Override
	public void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event) {
	}

}
