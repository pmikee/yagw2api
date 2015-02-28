package yagw2api.explorer.rcp.wvw;

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import yagw2api.explorer.rcp.swt.TypeSafeContentProvider;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWWrapper;

final class MatchesContentProvider extends TypeSafeContentProvider<IWVWWrapper> implements IStructuredContentProvider {
	public MatchesContentProvider() {
		super(IWVWWrapper.class);
	}

	@Override
	public Object[] getTypeSafeElements(final IWVWWrapper inputElement) {
		final Set<IWVWMatch> matches = inputElement.getAllMatches();
		return matches.toArray(new IWVWMatch[matches.size()]);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void typeSafeInputChanged(final Viewer viewer, final IWVWWrapper oldInput, final IWVWWrapper newInput) {
	}
}