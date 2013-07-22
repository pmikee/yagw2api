package de.justi.yagw2api.explorer.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.swing.table.AbstractTableModel;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import de.justi.yagw2api.mumblelink.IMumbleLink;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraFrontChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraPositionChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkCameraTopChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkMapChangeEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;
import de.justi.yagw2api.mumblelink.impl.IMumbleLinkListener;

public class MumbleLinkTableModel extends AbstractTableModel implements IMumbleLinkListener {
	private static final long serialVersionUID = -5095699668587612370L;
	private final IMumbleLink mumbleLink;

	private Optional<String> avatarName = Optional.absent();
	private Optional<Integer> mapId = Optional.absent();
	private Optional<IMumbleLinkPosition> avatarPosition = Optional.absent();

	@Inject
	public MumbleLinkTableModel(IMumbleLink mumbleLink) {
		this.mumbleLink = checkNotNull(mumbleLink);
		this.mumbleLink.registerMumbleLinkListener(this);
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		checkArgument(rowIndex == 0);
		switch (columnIndex) {
			case 0:
				return this.avatarName.or("");
			case 1:
				return this.mapId.orNull();
			case 2:
				if (this.avatarPosition.isPresent()) {
					return this.avatarPosition.get().getX();
				}
				break;
			case 3:
				if (this.avatarPosition.isPresent()) {
					return this.avatarPosition.get().getY();
				}
				break;
			case 4:
				if (this.avatarPosition.isPresent()) {
					return this.avatarPosition.get().getZ();
				}
				break;
		}
		return null;
	}

	@Override
	public void onAvatarChange(IMumbleLinkAvatarChangeEvent event) {
		this.avatarName = event.getNewAvatarName();
		this.fireTableDataChanged();
	}

	@Override
	public void onMapChange(IMumbleLinkMapChangeEvent event) {
		this.mapId = event.getNewMapId();
		this.fireTableDataChanged();
	}

	@Override
	public void onAvatarPositionChange(IMumbleLinkAvatarPositionChangeEvent event) {
		this.avatarPosition = event.getNewPosition();
		this.fireTableDataChanged();
	}

	@Override
	public void onAvatarFrontChange(IMumbleLinkAvatarFrontChangeEvent event) {
		// nothing to do
	}

	@Override
	public void onAvatarTopChange(IMumbleLinkAvatarTopChangeEvent event) {
		// nothing to do
	}

	@Override
	public void onCameraPositionChange(IMumbleLinkCameraPositionChangeEvent event) {
		// nothing to do
	}

	@Override
	public void onCameraFrontChange(IMumbleLinkCameraFrontChangeEvent event) {
		// nothing to do
	}

	@Override
	public void onCameraTopChange(IMumbleLinkCameraTopChangeEvent event) {
		// nothing to do
	}
}
