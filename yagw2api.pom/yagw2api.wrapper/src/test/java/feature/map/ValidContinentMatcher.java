package feature.map;

import static com.google.common.base.Preconditions.checkNotNull;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import de.justi.yagw2api.wrapper.map.domain.Continent;
import de.justi.yagw2api.wrapper.map.domain.MapFloor;

final class ValidContinentMatcher extends TypeSafeMatcher<Continent> {
	// STATIC
	public static ValidContinentMatcher isValidContinent() {
		return new ValidContinentMatcher();
	}

	// CONSTRUCTOR
	ValidContinentMatcher() {
	}

	// METHODS
	@Override
	public void describeTo(final Description description) {
		checkNotNull(description, "missing description");
		description.appendText("a valid continent");
	}

	@Override
	protected void describeMismatchSafely(final Continent item, final Description mismatchDescription) {
		checkNotNull(item, "missing item");
		checkNotNull(mismatchDescription, "missing mismatchDescription");
		mismatchDescription.appendText("(");
		mismatchDescription.appendValue(item);
		mismatchDescription.appendText(") ");
		if (item.getDimension() == null) {
			mismatchDescription.appendText("is missing dimension");
		}
		if (item.getId() == null) {
			mismatchDescription.appendText("is missing id");
		}
		if (item.getName() == null) {
			mismatchDescription.appendText("is missing name");
		}
		if (item.getMap() == null) {
			mismatchDescription.appendText("is missing map");
		}
		if (item.getMap().getFloors() == null) {
			mismatchDescription.appendText("is missing map floors");
		} else {
			for (MapFloor floor : item.getMap().getFloors()) {
				if (item.getMap().getFloorTiles(floor) == null) {
					mismatchDescription.appendText("is missing tiles for " + floor);
				}
			}
		}
	}

	@Override
	protected boolean matchesSafely(final Continent item) {
		checkNotNull(item, "missing item");
		System.out.println(item);
		if (item.getDimension() == null) {
			return false;
		}
		if (item.getId() == null) {
			return false;
		}
		if (item.getName() == null) {
			return false;
		}
		if (item.getMap() == null) {
			return false;
		}
		if (item.getMap().getFloors() == null) {
			return false;
		}
		for (MapFloor floor : item.getMap().getFloors()) {
			if (item.getMap().getFloorTiles(floor) == null) {
				return false;
			}
		}
		return true;
	}
}