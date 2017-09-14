package org.testobject.appium.sauceconnect;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.testobject.rest.api.appium.common.TestObjectCapabilities.TESTOBJECT_API_KEY;
import static org.testobject.rest.api.appium.common.TestObjectCapabilities.TESTOBJECT_TEST_NAME;

class AbstractTest {

	DesiredCapabilities createCapabilities(String apiKey) {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability(TESTOBJECT_API_KEY, apiKey);
		capabilities.setCapability(TESTOBJECT_TEST_NAME, "sc-appium-test");
		capabilities.setCapability("tunnelIdentifier", getEnv("TUNNEL_IDENTIFIER"));
		capabilities.setCapability("TESTOBJECT_UUID", UUID.randomUUID().toString());
		return capabilities;
	}

	static String getEnv(String key) {
		String value = System.getenv(key);
		if (value == null) {
			throw new RuntimeException("Missing env var " + key);
		} else {
			return value;
		}
	}

	static URL getAppiumServer() {
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
