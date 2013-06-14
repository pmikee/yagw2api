package de.justi.yagw2api.sample.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

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
import org.noos.xing.mydoggy.DockedTypeDescriptor;
import org.noos.xing.mydoggy.PushAwayMode;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.ToolWindowManagerDescriptor;
import org.noos.xing.mydoggy.ToolWindowType;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.sample.model.MapObjectivesTableModel;
import de.justi.yagw2api.sample.model.MatchDetailsTableModel;
import de.justi.yagw2api.sample.model.MatchesTableModel;
import de.justi.yagw2api.sample.renderer.MatchDetailsTableCellRenderer;
import de.justi.yagw2api.sample.renderer.ObjectiveTableCellRenderer;

public class MainWindow extends AbstractWindow {
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

	private JTable allMapsTable;
	private final MapObjectivesTableModel allMapsModel;

	private JTable greenTable;
	private final MapObjectivesTableModel greenMapModel;
	private JTable blueTable;
	private final MapObjectivesTableModel blueMapModel;
	private JTable redTable;
	private final MapObjectivesTableModel redMapModel;
	private final ToolWindow matchesToolWindow;
	private final ToolWindow allMapsToolWindow;
	private ToolWindow eternalMapToolWindow;
	private final ToolWindow blueMapToolWindow;
	private final ToolWindow greenMapToolWindow;
	private final ToolWindow redMapToolWindow;
	private final ToolWindow matchDetailsToolWindow;
	private final MyDoggyToolWindowManager toolWindowManager;

	public MainWindow() {
		super();
		this.setTitle("Yet Another GW2 API - Sample Application");
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

		this.toolWindowManager = new MyDoggyToolWindowManager();
		this.toolWindowManager.setDockableMainContentMode(true);
		this.toolWindowManager.setBarsTemporarilyVisible(true);
		this.getContentPanel().add(this.toolWindowManager, BorderLayout.CENTER);
		final ToolWindowManagerDescriptor toolWindowManagerDesc = this.toolWindowManager.getToolWindowManagerDescriptor();
		toolWindowManagerDesc.setNumberingEnabled(false);
		toolWindowManagerDesc.setPushAwayMode(PushAwayMode.MOST_RECENT);

		this.matchesTable = this.initMatchesTable(this.matchesTableModel);
		this.matchesToolWindow = this.toolWindowManager.registerToolWindow("Matches Overview", "Matches Overview", null, new JScrollPane(this.matchesTable), ToolWindowAnchor.LEFT);
		this.matchesToolWindow.setVisible(true);
		final DockedTypeDescriptor matchesToolWindowDescriptor = (DockedTypeDescriptor) this.matchesToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		matchesToolWindowDescriptor.setIdVisibleOnTitleBar(false);		

		this.allMapsTable = this.initMapTable(this.allMapsModel);
		this.allMapsToolWindow = this.toolWindowManager.registerToolWindow("All Maps", "All Maps", null, new JScrollPane(this.allMapsTable), ToolWindowAnchor.RIGHT);
		this.allMapsToolWindow.setVisible(true);
		final DockedTypeDescriptor allMapsToolWindowDescriptor = (DockedTypeDescriptor) this.allMapsToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		allMapsToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.eternalTable = this.initMapTable(this.eternalMapModel);
		this.eternalMapToolWindow = this.toolWindowManager.registerToolWindow("Enternal Battlegrounds", "Enternal Battlegrounds", null, new JScrollPane(this.eternalTable), ToolWindowAnchor.RIGHT);
		this.eternalMapToolWindow.setVisible(true);
		final DockedTypeDescriptor eternalMapToolWindowDescriptor = (DockedTypeDescriptor) this.eternalMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		eternalMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.blueTable = this.initMapTable(this.blueMapModel);
		this.blueMapToolWindow = this.toolWindowManager.registerToolWindow("Blue Borderlands", "Blue Borderlands", null, new JScrollPane(this.blueTable), ToolWindowAnchor.RIGHT);
		this.blueMapToolWindow.setVisible(true);
		final DockedTypeDescriptor blueMapToolWindowDescriptor = (DockedTypeDescriptor) this.blueMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		blueMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.greenTable = this.initMapTable(this.greenMapModel);
		this.greenMapToolWindow = this.toolWindowManager.registerToolWindow("Green Borderlands", "Green Borderlands", null, new JScrollPane(this.greenTable), ToolWindowAnchor.RIGHT);
		this.greenMapToolWindow.setVisible(true);
		final DockedTypeDescriptor greenMapToolWindowDescriptor = (DockedTypeDescriptor) this.greenMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		greenMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.redTable = this.initMapTable(this.redMapModel);
		this.redMapToolWindow = this.toolWindowManager.registerToolWindow("Red Borderlands", "Red Borderlands", null, new JScrollPane(this.redTable), ToolWindowAnchor.RIGHT);
		this.redMapToolWindow.setVisible(true);
		final DockedTypeDescriptor redMapToolWindowDescriptor = (DockedTypeDescriptor) this.redMapToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		redMapToolWindowDescriptor.setIdVisibleOnTitleBar(false);

		this.matchDetailslTable = this.initMatchDetailsTable(this.matchDetailsTableModel);
		this.matchDetailsToolWindow = this.toolWindowManager.registerToolWindow("Match Details", "Match Details", null, new JScrollPane(this.matchDetailslTable), ToolWindowAnchor.BOTTOM);
		this.matchDetailsToolWindow.setVisible(true);
		final DockedTypeDescriptor matchDetailsToolWindowDescriptor = (DockedTypeDescriptor) this.matchDetailsToolWindow.getTypeDescriptor(ToolWindowType.DOCKED);
		matchDetailsToolWindowDescriptor.setIdVisibleOnTitleBar(false);
		matchDetailsToolWindowDescriptor.setTitleBarButtonsVisible(false);
		matchDetailsToolWindowDescriptor.setTitleBarVisible(false);
		this.pack();
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

}
