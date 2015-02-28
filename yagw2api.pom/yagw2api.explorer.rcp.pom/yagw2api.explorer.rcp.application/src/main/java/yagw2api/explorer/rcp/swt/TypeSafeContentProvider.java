package yagw2api.explorer.rcp.swt;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TypeSafeContentProvider<T> implements IStructuredContentProvider {
	private final Class<T> dataTypeClass;

	public TypeSafeContentProvider(final Class<T> dataTypeClass) {
		this.dataTypeClass = checkNotNull(dataTypeClass, "missing datatype class");
	}

	@Override
	public final Object[] getElements(final Object inputElement) {
		checkArgument(this.dataTypeClass.isInstance(inputElement), "expected %s to be instance of %s", inputElement, this.dataTypeClass);
		return this.getTypeSafeElements(this.dataTypeClass.cast(inputElement));
	}

	protected Object[] getTypeSafeElements(final T inputElement) {
		return new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public final void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		checkArgument(oldInput == null || this.dataTypeClass.isInstance(oldInput), "expected %s to be instance of %s", oldInput, this.dataTypeClass);
		checkArgument(newInput == null || this.dataTypeClass.isInstance(newInput), "expected %s to be instance of %s", newInput, this.dataTypeClass);
		this.typeSafeInputChanged(viewer, oldInput == null ? null : this.dataTypeClass.cast(oldInput), newInput == null ? null : this.dataTypeClass.cast(newInput));
	}

	protected void typeSafeInputChanged(final Viewer viewer, final T oldInput, final T newInput) {

	}
}