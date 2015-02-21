package yagw2api.explorer.rcp.swt;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TypeSafeViewerLabelProvider<T> extends LabelProvider {
	private final Class<T> dataTypeClass;

	public TypeSafeViewerLabelProvider(final Class<T> dataTypeClass) {
		this.dataTypeClass = checkNotNull(dataTypeClass, "missing datatype class");
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

	protected Image getTypeSafeImage(final T o) {
		return super.getImage(o);
	}

	protected String getTypeSafeText(final T element) {
		return super.getText(element);
	}
}