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

	private final boolean draggable;

	public MemoryMonitorDockableDescriptor(MyDoggyToolWindowManager manager, ToolWindowAnchor anchor, boolean draggable) {
		super(manager, anchor);
		this.draggable = draggable;
	}

	@Override
	public void updateRepresentativeAnchor() {
	}

	@Override
	public synchronized JComponent getRepresentativeAnchor(Component parent) {
		if (this.representativeAnchor == null) {
			this.representativeAnchor = new MemoryMonitorPanel(anchor, this.draggable);
		}
		return this.representativeAnchor;
	}

	@Override
	public boolean isAvailableCountable() {
		return false;
	}

	private class MemoryMonitorPanel extends JPanel {
		private static final int SLEEP = 1000;
		private static final long serialVersionUID = -5081978515639631610L;

		public MemoryMonitorPanel(ToolWindowAnchor anchor, boolean draggable) {
			final JProgressBar memoryUsage = new JProgressBar();
			memoryUsage.setStringPainted(true);

			final Thread memoryThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						final String grabbed = StringUtil.bytes2MBytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
						final String total = StringUtil.bytes2MBytes(Runtime.getRuntime().totalMemory());
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
					this.setLayout(new TableLayout(new double[][] { { 120, -1 }, { -1 } }));
					this.add(memoryUsage, "0,0,FULL,FULL");
					break;
				case LEFT:
				case RIGHT:
					memoryUsage.setOrientation(SwingConstants.VERTICAL);
					this.setLayout(new TableLayout(new double[][] { { -1 }, { 120, -1 } }));
					this.add(memoryUsage, "0,0,FULL,FULL");
					break;
			}

			if (draggable) {
				MemoryMonitorDockableDescriptor.this.registerDragListener(memoryUsage);
				MemoryMonitorDockableDescriptor.this.registerDragListener(this);
			}
		}
	}

}