package org.testobject.appium.sauceconnect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

class WebTest extends AbstractTest {
	private RemoteWebDriver driver;

	@BeforeEach
	void setUp() {
		String apiKey = getEnv("TESTOBJECT_API_KEY_WEB");
		DesiredCapabilities desiredCapabilities = createCapabilities(apiKey);
		desiredCapabilities.setCapability("platformName", getEnv("PLATFORM_WEB"));
		System.out.println("Initializing web test with capabilities: " + desiredCapabilities);
		driver = new RemoteWebDriver(getAppiumServer(), desiredCapabilities);
		System.out.println("Returned capabilities: " + driver.getCapabilities());
	}

	@Test
	void getPageSource() throws InterruptedException {
		String url = getEnv("DESTINATION_URL");
		System.out.println("Loading " + url);
		driver.get(url);
		Thread.sleep(10000);
		System.out.println(driver.getPageSource());
	}

	@AfterEach
	void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
