package yagw2api.explorer.rcp.swt;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class TypeSafeColumnLabelProvider<T> extends ColumnLabelProvider {
	private final Class<T> dataTypeClass;

	public TypeSafeColumnLabelProvider(final Class<T> dataTypeClass) {
		this.dataTypeClass = checkNotNull(dataTypeClass, "missing datatype class");
	}

	@Override
	public final Font getFont(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeFont(this.dataTypeClass.cast(element));
	}

	@Override
	public final Color getBackground(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeBackground(this.dataTypeClass.cast(element));
	}

	@Override
	public final Color getForeground(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeForeground(this.dataTypeClass.cast(element));
	}

	@Override
	public final Image getImage(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeImage(this.dataTypeClass.cast(element));
	}

	@Override
	public final String getText(final Object element) {
		checkArgument(this.dataTypeClass.isInstance(element), "expected %s to be instance of %s", element, this.dataTypeClass);
		return this.getTypeSafeText(this.dataTypeClass.cast(element));
	}

	protected String getTypeSafeText(final T element) {
		return super.getText(element);
	}

	protected Image getTypeSafeImage(final T element) {
		return super.getImage(element);
	}

	protected Font getTypeSafeFont(final T element) {
		return super.getFont(element);
	}

	protected Color getTypeSafeBackground(final T element) {
		return super.getBackground(element);
	}

	protected Color getTypeSafeForeground(final T element) {
		return super.getForeground(element);
	}
}