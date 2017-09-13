package org.testobject.appium.sauceconnect;

import org.junit.jupiter.api.BeforeEach;

public class IosTest extends AbstractTest {
	@BeforeEach
	void setUp() {
		String apiKey = getEnv("TESTOBJECT_API_KEY_IOS");
		initIosDriver(apiKey);
	}
}
