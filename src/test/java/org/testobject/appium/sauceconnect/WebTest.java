package org.testobject.appium.sauceconnect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebTest extends AbstractTest {
	@BeforeEach
	void setUp() {
		String apiKey = getEnv("TESTOBJECT_API_KEY_WEB");
		initDriver(apiKey);
	}

	@Test
	void getPageSource() throws InterruptedException {
		String url = getEnv("DESTINATION_URL");
		driver.get(url);
		Thread.sleep(2000);
		System.out.println(driver.getPageSource());
	}
}
