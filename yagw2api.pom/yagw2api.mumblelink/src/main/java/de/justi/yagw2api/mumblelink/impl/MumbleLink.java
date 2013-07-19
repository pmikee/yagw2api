package de.justi.yagw2api.mumblelink.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;

import de.justi.yagw2api.mumblelink.IMumbleLink;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;

final class MumbleLink implements IMumbleLink {
	private static final int MEM_MAP_SIZE = 5460;
	private static final String MEM_MAP_NAME = "MumbleLink";

	private static final class MumbleLinkPosition implements IMumbleLinkPosition {
		private static Optional<IMumbleLinkPosition> of(float[] positionArray) {
			if (positionArray == null) {
				return Optional.absent();
			} else {
				checkArgument(positionArray.length == 3);
				return Optional.<IMumbleLinkPosition> of(new MumbleLinkPosition(positionArray[0], positionArray[1], positionArray[2]));
			}
		}

		private final float x;
		private final float y;
		private final float z;

		private MumbleLinkPosition(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public float getX() {
			return this.x;
		}

		@Override
		public float getY() {
			return this.y;
		}

		@Override
		public float getZ() {
			return this.z;
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("x", this.x).add("y", this.y).add("z", this.z).toString();
		}
	}

	private volatile Optional<Integer> uiVersion = Optional.absent();
	private volatile Optional<Integer> uiTick = Optional.absent();
	private volatile Optional<IMumbleLinkPosition> avatarPosition = Optional.absent();
	private volatile Optional<IMumbleLinkPosition> avatarFront = Optional.absent();
	private volatile Optional<IMumbleLinkPosition> avatarTop = Optional.absent();
	private volatile Optional<IMumbleLinkPosition> cameraPosition = Optional.absent();
	private volatile Optional<IMumbleLinkPosition> cameraFront = Optional.absent();
	private volatile Optional<IMumbleLinkPosition> cameraTop = Optional.absent();
	private volatile Optional<String> avatarName = Optional.absent();
	private volatile Optional<String> gameName = Optional.absent();
	private volatile Optional<Integer> contextLength = Optional.absent();
	private volatile Optional<byte[]> context = Optional.absent();

	private final AbstractScheduledService mumbleLinkUpdater;

	MumbleLink() {
		this.mumbleLinkUpdater = new AbstractScheduledService() {

			private volatile Pointer sharedMemory = null;
			private volatile HANDLE sharedFile = null;

			@Override
			protected Scheduler scheduler() {
				return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, 100, TimeUnit.MILLISECONDS);
			}

			@Override
			protected void runOneIteration() throws Exception {
				if (this.sharedFile == null) {
					synchronized (this) {
						if (this.sharedFile == null) {
							this.sharedFile = Kernel32.INSTANCE.CreateFileMapping(WinBase.INVALID_HANDLE_VALUE, null, WinNT.PAGE_EXECUTE_READWRITE, 0, MEM_MAP_SIZE, MEM_MAP_NAME);
						}
					}
				}
				if ((this.sharedFile != null) && (this.sharedMemory == null)) {
					synchronized (this) {
						if (this.sharedMemory == null) {
							this.sharedMemory = Kernel32.INSTANCE.MapViewOfFile(this.sharedFile, WinNT.SECTION_MAP_READ, 0, 0, MEM_MAP_SIZE);
						}
					}
				}

				synchronized (MumbleLink.this) {
					if (this.sharedMemory != null) {
						MumbleLink.this.uiVersion = Optional.of(this.sharedMemory.getInt(0));
						MumbleLink.this.uiTick = Optional.of(this.sharedMemory.getInt(4));
						MumbleLink.this.avatarPosition = MumbleLinkPosition.of(this.sharedMemory.getFloatArray(8, 3));
						MumbleLink.this.avatarFront = MumbleLinkPosition.of(this.sharedMemory.getFloatArray(20, 3));
						MumbleLink.this.avatarTop = MumbleLinkPosition.of(this.sharedMemory.getFloatArray(32, 3));
						MumbleLink.this.gameName = Optional.of(String.copyValueOf(this.sharedMemory.getCharArray(44, 256)).trim());
						MumbleLink.this.cameraPosition = MumbleLinkPosition.of(this.sharedMemory.getFloatArray(556, 3));
						MumbleLink.this.cameraFront = MumbleLinkPosition.of(this.sharedMemory.getFloatArray(568, 3));
						MumbleLink.this.cameraTop = MumbleLinkPosition.of(this.sharedMemory.getFloatArray(580, 3));
						MumbleLink.this.avatarName = Optional.of(String.copyValueOf(this.sharedMemory.getCharArray(592, 256)).trim());
						MumbleLink.this.contextLength = Optional.of(this.sharedMemory.getInt(1104));
						MumbleLink.this.context = Optional.of(this.sharedMemory.getByteArray(1108, 256));
					} else {
						MumbleLink.this.uiVersion = Optional.absent();
						MumbleLink.this.uiTick = Optional.absent();
						MumbleLink.this.avatarPosition = Optional.absent();
						MumbleLink.this.avatarFront = Optional.absent();
						MumbleLink.this.avatarTop = Optional.absent();
						MumbleLink.this.gameName = Optional.absent();
						MumbleLink.this.cameraPosition = Optional.absent();
						MumbleLink.this.cameraFront = Optional.absent();
						MumbleLink.this.cameraTop = Optional.absent();
						MumbleLink.this.avatarName = Optional.absent();
						MumbleLink.this.contextLength = Optional.absent();
						MumbleLink.this.context = Optional.absent();
					}

				}
			}
		};
		this.mumbleLinkUpdater.start();
	}

	@Override
	public void setActive(boolean active) {
		if (active != this.isActive()) {
			if (active) {
				this.mumbleLinkUpdater.startAndWait();
			} else {
				this.mumbleLinkUpdater.stopAndWait();
			}
		}
	}

	@Override
	public Optional<Integer> getUIVersion() {
		return this.uiVersion;
	}

	@Override
	public Optional<Integer> getUITick() {
		return this.uiTick;
	}

	@Override
	public boolean isActive() {
		return this.mumbleLinkUpdater.isRunning();
	}

	@Override
	public Optional<String> getGameName() {
		return this.gameName;
	}

	@Override
	public Optional<String> getAvatarName() {
		return this.avatarName;
	}

	@Override
	public Optional<IMumbleLinkPosition> getAvatarPosition() {
		return this.avatarPosition;
	}

	@Override
	public Optional<IMumbleLinkPosition> getAvatarFront() {
		return this.avatarFront;
	}

	@Override
	public Optional<IMumbleLinkPosition> getAvatarTop() {
		return this.avatarTop;
	}

	@Override
	public Optional<IMumbleLinkPosition> getCameraPosition() {
		return this.cameraPosition;
	}

	@Override
	public Optional<IMumbleLinkPosition> getCameraFront() {
		return this.cameraFront;
	}

	@Override
	public Optional<IMumbleLinkPosition> getCameraTop() {
		return this.cameraTop;
	}

	@Override
	public Optional<Integer> getContextLength() {
		return this.contextLength;
	}

	@Override
	public Optional<byte[]> getContext() {
		return this.context;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("active", this.isActive()).toString();
	}

}
