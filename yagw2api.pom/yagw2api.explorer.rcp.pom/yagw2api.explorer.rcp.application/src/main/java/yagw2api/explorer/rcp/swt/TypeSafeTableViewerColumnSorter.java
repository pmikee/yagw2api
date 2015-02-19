package yagw2api.explorer.rcp.swt;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wb.swt.TableViewerColumnSorter;

public class TypeSafeTableViewerColumnSorter<T> extends TableViewerColumnSorter {

	private final Class<T> dataTypeClass;

	public TypeSafeTableViewerColumnSorter(final TableViewerColumn column, final Class<T> dataTypeClass) {
		super(column);
		this.dataTypeClass = checkNotNull(dataTypeClass, "missing datatype class");
	}

	@Override
	protected final int doCompare(final Viewer viewer, final Object e1, final Object e2) {
		checkArgument(this.dataTypeClass.isInstance(e1), "expected %s to be instance of %s", e1, this.dataTypeClass);
		checkArgument(this.dataTypeClass.isInstance(e2), "expected %s to be instance of %s", e2, this.dataTypeClass);
		return this.doCompareTypeSafe(viewer, this.dataTypeClass.cast(e1), this.dataTypeClass.cast(e2));
	}

	@Override
	protected final Object getValue(final Object o) {
		checkArgument(this.dataTypeClass.isInstance(o), "expected %s to be instance of %s", o, this.dataTypeClass);
		return this.getTypeSafeValue(this.dataTypeClass.cast(o));
	}

	protected Object getTypeSafeValue(final T o) {
		return super.getValue(o);
	}

	protected int doCompareTypeSafe(final Viewer viewer, final T e1, final T e2) {
		return super.doCompare(viewer, e1, e2);
	}

}
