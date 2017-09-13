package org.testobject.appium.sauceconnect;

import org.junit.jupiter.api.BeforeEach;

public class AndroidTest extends AbstractTest {
	@BeforeEach
	void setUp() {
		String apiKey = getEnv("TESTOBJECT_API_KEY_ANDROID");
		initAndroidDriver(apiKey);
	}
}
