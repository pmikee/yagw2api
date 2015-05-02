package de.justi.yagw2api.wrapper.map;

import org.junit.Test;

import de.justi.yagw2api.wrapper.domain.map.Continent;

public class DefaultWrapperIT {

	@Test
	public void test() {
		final DefaultMapWrapper wrapper = DefaultMapWrapper.INSTANCE;
		for (Continent continent : wrapper.getContinents()) {
			System.out.println(continent);
		}
	}
}
