package org.testobject.appium.sauceconnect;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.function.Function;

import static org.testobject.rest.api.appium.common.TestObjectCapabilities.TESTOBJECT_API_KEY;
import static org.testobject.rest.api.appium.common.TestObjectCapabilities.TESTOBJECT_TEST_NAME;

class AbstractTest {

	enum DRIVER {
		IOSDRIVER(capabilities -> new IOSDriver<>(getAppiumServer(), capabilities)),
		ANDROIDDRIVER(capabilities -> new AndroidDriver<>(getAppiumServer(), capabilities));

		private final Function<DesiredCapabilities, RemoteWebDriver> factory;

		DRIVER(Function<DesiredCapabilities, RemoteWebDriver> factory) {
			this.factory = factory;
		}

		private RemoteWebDriver get(DesiredCapabilities capabilities) {
			return factory.apply(capabilities);
		}
	}

	RemoteWebDriver driver;

	void initDriver(String apiKey) {
		DRIVER type = DRIVER.valueOf(getEnv("DRIVER").toUpperCase());
		initDriver(apiKey, type);
	}

	void initAndroidDriver(String apiKey) {
		initDriver(apiKey, DRIVER.ANDROIDDRIVER);
	}

	void initIosDriver(String apiKey) {
		initDriver(apiKey, DRIVER.IOSDRIVER);
	}

	private void initDriver(String apiKey, DRIVER driverType) {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability(TESTOBJECT_API_KEY, apiKey);
		capabilities.setCapability(TESTOBJECT_TEST_NAME, "sc-appium-test");
		capabilities.setCapability("tunnelIdentifier", getEnv("TUNNEL_IDENTIFIER"));
		capabilities.setCapability("TESTOBJECT_UUID", UUID.randomUUID().toString());

		System.out.print("Initializing driver with DesiredCapabilities:\n" + capabilities + "\n");
		driver = driverType.get(capabilities);
		System.out.print("Driver initialized. Returned capabilities:\n" + driver.getCapabilities() + "\n");
	}

	@AfterEach
	void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	static String getEnv(String key) {
		String value = System.getenv(key);
		if (value == null) {
			throw new RuntimeException("Missing env var " + key);
		} else {
			return value;
		}
	}

	private static URL getAppiumServer() {
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
