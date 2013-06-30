package de.justi.yagw2api.sample.view;

import info.clearthought.layout.TableLayout;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.noos.xing.mydoggy.plaf.ui.CustomDockableDescriptor;
import org.noos.xing.mydoggy.plaf.ui.util.StringUtil;

public class MemoryMonitorDockableDescriptor extends CustomDockableDescriptor {

	public MemoryMonitorDockableDescriptor(MyDoggyToolWindowManager manager, ToolWindowAnchor anchor) {
		super(manager, anchor);
	}

	@Override
	public void updateRepresentativeAnchor() {
	}

	@Override
	public JComponent getRepresentativeAnchor(Component parent) {
		if (representativeAnchor == null) {
			representativeAnchor = new MemoryMonitorPanel(anchor);
		}
		return representativeAnchor;
	}

	@Override
	public boolean isAvailableCountable() {
		return false;
	}

	private class MemoryMonitorPanel extends JPanel {
		private static final int SLEEP = 1000;
		private static final long serialVersionUID = -5081978515639631610L;

		public MemoryMonitorPanel(ToolWindowAnchor anchor) {
			final JProgressBar memoryUsage = new JProgressBar();
			memoryUsage.setStringPainted(true);

			final Thread memoryThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						String grabbed = StringUtil.bytes2MBytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
						String total = StringUtil.bytes2MBytes(Runtime.getRuntime().totalMemory());

						memoryUsage.setMaximum(Integer.parseInt(total));
						memoryUsage.setValue(Integer.parseInt(grabbed));

						memoryUsage.setString(grabbed + " MB of " + total + " MB");
						try {
							Thread.sleep(SLEEP);
						} catch (InterruptedException e) {
						}
					}
				}
			});
			memoryThread.setDaemon(true);
			memoryThread.setPriority(Thread.MIN_PRIORITY);
			memoryThread.start();

			switch (anchor) {
				case BOTTOM:
				case TOP:
					memoryUsage.setOrientation(SwingConstants.HORIZONTAL);
					setLayout(new TableLayout(new double[][] { { 120, 1, 17 }, { -1 } }));
					add(memoryUsage, "0,0,FULL,FULL");
					break;
				case LEFT:
					memoryUsage.setOrientation(SwingConstants.VERTICAL);
					setLayout(new TableLayout(new double[][] { { -1 }, { 120, 1, 17 } }));
					add(memoryUsage, "0,0,FULL,FULL");
					break;
				case RIGHT:
					memoryUsage.setOrientation(SwingConstants.VERTICAL);
					setLayout(new TableLayout(new double[][] { { -1 }, { 17, 1, 120 } }));
					add(memoryUsage, "0,2,FULL,FULL");
					break;
			}

			registerDragListener(memoryUsage);
			registerDragListener(this);
		}
	}

}