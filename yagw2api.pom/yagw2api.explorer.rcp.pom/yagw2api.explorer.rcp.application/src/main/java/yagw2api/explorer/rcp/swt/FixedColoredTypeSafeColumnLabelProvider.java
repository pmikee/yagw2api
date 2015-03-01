package yagw2api.explorer.rcp.swt;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wb.swt.SWTResourceManager;

public class FixedColoredTypeSafeColumnLabelProvider<T> extends TypeSafeColumnLabelProvider<T> {

	private final Color backgroundColor;
	private final Color foregroundColor;

	public FixedColoredTypeSafeColumnLabelProvider(final Class<T> dataTypeClass, final RGB background, final RGB foreground) {
		super(checkNotNull(dataTypeClass, "missing data type class"));
		this.backgroundColor = SWTResourceManager.getColor(checkNotNull(background, "missing background"));
		this.foregroundColor = SWTResourceManager.getColor(checkNotNull(foreground, "missing foreground"));
	}

	@Override
	protected Color getTypeSafeBackground(final T element) {
		return this.backgroundColor;
	}

	@Override
	protected Color getTypeSafeForeground(final T element) {
		return this.foregroundColor;
	}
}