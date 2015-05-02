package de.justi.yagw2api.wrapper.map;

import org.junit.Test;

public class DefaultWrapperIT {

	@Test
	public void test() {
		final DefaultMapWrapper wrapper = DefaultMapWrapper.INSTANCE;
		wrapper.getContinents();
	}
}
