package org.testobject.appium.sauceconnect;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.testobject.rest.api.appium.common.TestObjectCapabilities.TESTOBJECT_API_KEY;
import static org.testobject.rest.api.appium.common.TestObjectCapabilities.TESTOBJECT_TEST_NAME;

public class SauceConnectTest {
	private RemoteWebDriver driver;

	@BeforeEach
	void setUp() {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability(TESTOBJECT_API_KEY, getEnv(TESTOBJECT_API_KEY));
		capabilities.setCapability(TESTOBJECT_TEST_NAME, "sc-appium-test");
		capabilities.setCapability("tunnelIdentifier", getEnv("TUNNEL_IDENTIFIER"));
		capabilities.setCapability("TESTOBJECT_UUID", UUID.randomUUID().toString());

		System.out.print("Initializing driver with DesiredCapabilities:\n" + capabilities + "\n");
		driver = initDriver(capabilities);
		System.out.print("Driver initialized. Returned capabilities:\n" + driver.getCapabilities() + "\n");
	}

	@Test
	void getPageSource() {
		System.out.println(driver.getPageSource());
	}

	@AfterEach
	void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	private String getEnv(String key) {
		String value = System.getenv(key);
		if (key == null) {
			throw new RuntimeException("Missing env var " + key);
		} else {
			return value;
		}
	}

	private RemoteWebDriver initDriver(DesiredCapabilities capabilities) {
		String driver = System.getenv("DRIVER");
		if (driver == null || driver.isEmpty()) {
			throw new RuntimeException("Missing required environment variable: DRIVER");
		}
		switch (driver.toLowerCase()) {
		case "iosdriver":
			return new IOSDriver<>(getAppiumServer(), capabilities);
		case "androiddriver":
			return new AndroidDriver<>(getAppiumServer(), capabilities);
		case "remotewebdriver":
			return new RemoteWebDriver(getAppiumServer(), capabilities);
		default:
			throw new RuntimeException("Unrecognized DRIVER variable: " + driver);
		}
	}

	private URL getAppiumServer() {
		String url = System.getenv("APPIUM_URL");
		if (url == null || url.isEmpty()) {
			throw new RuntimeException("Missing required environment variable APPIUM_URL");
		} else {
			try {
				return new URL(url);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid URL: " + url, e);
			}
		}
	}
}
