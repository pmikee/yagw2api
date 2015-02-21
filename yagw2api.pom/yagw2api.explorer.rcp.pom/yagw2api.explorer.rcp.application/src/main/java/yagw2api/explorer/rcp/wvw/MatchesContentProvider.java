package yagw2api.explorer.rcp.wvw;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;

final class MatchesContentProvider implements IStructuredContentProvider, IWVWMatchListener {
	private Optional<IWVWWrapper> currentInput = Optional.absent();
	private final List<IWVWMatch> matches = Lists.newArrayList();

	@Override
	public Object[] getElements(final Object inputElement) {
		return this.matches.toArray(new IWVWMatch[this.matches.size()]);
	}

	@Override
	public void dispose() {
		if (this.currentInput.isPresent()) {
			this.currentInput.get().unregisterWVWMatchListener(this);
		}
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		this.matches.clear();
		if (oldInput != null) {
			checkArgument(oldInput instanceof IWVWWrapper);
			((IWVWWrapper) oldInput).unregisterWVWMatchListener(this);
		}
		if (newInput != null) {
			checkArgument(newInput instanceof IWVWWrapper);
			this.currentInput = Optional.of(((IWVWWrapper) newInput));
			this.currentInput.get().registerWVWMatchListener(this);
		}
	}

	@Override
	public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent e) {
		this.matches.add(e.getMatch());

	}

	@Override
	public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent e) {
		// NOP
	}
}