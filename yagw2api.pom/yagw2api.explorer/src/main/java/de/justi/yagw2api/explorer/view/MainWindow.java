package de.justi.yagw2api.explorer.view;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-Application
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.io.Closer;

import de.justi.yagw2api.anchorman.YAGW2APIAnchorman;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.explorer.model.MapObjectivesTableModel;
import de.justi.yagw2api.explorer.model.MatchDetailsTableModel;
import de.justi.yagw2api.explorer.model.MatchesTableModel;
import de.justi.yagw2api.explorer.model.MumbleLinkTableModel;
import de.justi.yagw2api.explorer.renderer.MatchDetailsTableCellRenderer;
import de.justi.yagw2api.explorer.renderer.ObjectiveTableCellRenderer;
import de.justi.yagw2api.mumblelink.YAGW2APIMumbleLink;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;

public final class MainWindow extends AbstractWindow implements IWVWMapListener {
	private static final Locale LOCALE_ES = Locale.forLanguageTag("es");
	private static final String WORKSPACE_XML_FILENAME = "workspace.xml";
	private static final long serialVersionUID = -6500541020042114865L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);
	public static final Color ETERNAL_BATTLEGROUNDS_FG = new Color(200, 130, 0);
	public static final Color ETERNAL_BATTLEGROUNDS_BG = new Color(200, 130, 0, 100);

	public static final Color NEUTRAL_FG = new Color(0,0,0);
	public static final Color NEUTRAL_BG = new Color(255, 255, 255, 100);
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
	private final ToolWindowGroup singleMapTableToolWindows;

	private final JTable mumbleLinkTable;
	private final MumbleLinkTableModel mumbleLinkTableModel;
	private final ToolWindow mumbleLinkToolWindow;


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
		this.mumbleLinkTableModel = new MumbleLinkTableModel(YAGW2APIMumbleLink.INSTANCE.getMumbleLink());

		this.getContentPanel().add(this.builtMainMenuBar(), BorderLayout.NORTH);

		this.toolWindowManager = new MyDoggyToolWindowManager();
		this.getContentPanel().add(this.toolWindowManager, BorderLayout.CENTER);
		final ToolWindowManagerDescriptor toolWindowManagerDesc = this.toolWindowManager.getToolWindowManagerDescriptor();
		toolWindowManagerDesc.setNumberingEnabled(false);
		toolWindowManagerDesc.setPushAwayMode(PushAwayMode.MOST_RECENT);

		this.toolWindowManager.resetMainContent();

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

		this.mumbleLinkTable = this.initMumbleLinkTable(this.mumbleLinkTableModel);
		this.mumbleLinkToolWindow = this.toolWindowManager.registerToolWindow("MumbleLink", "MumbleLink", null, new JScrollPane(this.mumbleLinkTable), ToolWindowAnchor.BOTTOM);
		this.mumbleLinkToolWindow.setVisible(true);
		this.mumbleLinkToolWindow.setAggregateMode(true);
		final DockedTypeDescriptor mumbleLinkToolWindowDescriptor = (DockedTypeDescriptor) this.mumbleLinkToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		mumbleLinkToolWindowDescriptor.setIdVisibleOnTitleBar(false);
		mumbleLinkToolWindowDescriptor.setTitleBarButtonsVisible(false);
		mumbleLinkToolWindowDescriptor.setTitleBarVisible(false);


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

		this.pack();

		// finally try to restore workspace setting
		this.loadWorkspaceSettings();
	}

	private JTable initMumbleLinkTable(final MumbleLinkTableModel tableModel) {
		final String[] header = { "Avatarname", "Current Map", "Position-X", "Position-Y", "Position-Z" };
		final JTable table = new JTable(tableModel, newTCM(header));
		return table;
	}


	private boolean loadWorkspaceSettings() {
		try{
			try(final FileInputStream loadSource = new FileInputStream(WORKSPACE_XML_FILENAME)){
				MainWindow.this.toolWindowManager.getPersistenceDelegate().merge(loadSource, MergePolicy.RESET);
				LOGGER.info("Successfull loaded workspace settings.");	
				return true;
			}
		} catch (IOException ex) {
			LOGGER.error("Failed to load workspace settings");
			return false;
		}
	}

	private boolean saveWorskapceSettings() {
		try {
			try(final FileOutputStream saveDestination = new FileOutputStream(WORKSPACE_XML_FILENAME)) {
				MainWindow.this.toolWindowManager.getPersistenceDelegate().save(saveDestination);
				LOGGER.info("Successfull saved workspace settings.");
				return true;
			}
		} catch (IOException ex) {
			LOGGER.error("Failed to save workspace settings");
			return false;
		}
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

		final JMenuItem enLocaleMenuItem = new JRadioButtonMenuItem(Locale.ENGLISH.getLanguage());
		enLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIArenanet.INSTANCE.setCurrentLocale(Locale.ENGLISH);
			}
		});

		enLocaleMenuItem.setSelected(YAGW2APIArenanet.INSTANCE.getCurrentLocale().equals(Locale.ENGLISH));
		settingLocaleMenu.add(enLocaleMenuItem);
		final JMenuItem deLocaleMenuItem = new JRadioButtonMenuItem(Locale.GERMANY.getLanguage());
		deLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIArenanet.INSTANCE.setCurrentLocale(Locale.GERMANY);
			}
		});
		deLocaleMenuItem.setSelected(YAGW2APIArenanet.INSTANCE.getCurrentLocale().getLanguage().equals(Locale.GERMANY.getLanguage()));
		settingLocaleMenu.add(deLocaleMenuItem);
		final JMenuItem frLocaleMenuItem = new JRadioButtonMenuItem(Locale.FRANCE.getLanguage());
		frLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIArenanet.INSTANCE.setCurrentLocale(Locale.FRANCE);
			}
		});
		frLocaleMenuItem.setSelected(YAGW2APIArenanet.INSTANCE.getCurrentLocale().getLanguage().equals(Locale.FRANCE.getLanguage()));
		settingLocaleMenu.add(frLocaleMenuItem);
		final JMenuItem esLocaleMenuItem = new JRadioButtonMenuItem(LOCALE_ES.getLanguage());
		esLocaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				YAGW2APIArenanet.INSTANCE.setCurrentLocale(LOCALE_ES);
			}
		});
		esLocaleMenuItem.setSelected(YAGW2APIArenanet.INSTANCE.getCurrentLocale().getLanguage().equals(LOCALE_ES.getLanguage()));
		settingLocaleMenu.add(esLocaleMenuItem);

		final ButtonGroup group = new ButtonGroup();
		group.add(enLocaleMenuItem);
		group.add(deLocaleMenuItem);
		group.add(frLocaleMenuItem);
		group.add(esLocaleMenuItem);

		settingsMenu.add(settingLocaleMenu);
		mainMenuBar.add(settingsMenu);
		return mainMenuBar;
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
								YAGW2APIAnchorman.INSTANCE.getAnchorman().setWVWMatchFilter(match);
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
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
	}

	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
	}

	@Override
	public void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event) {
	}

}
