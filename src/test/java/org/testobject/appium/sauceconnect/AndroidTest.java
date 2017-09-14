package org.testobject.appium.sauceconnect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AndroidTest extends AbstractTest {
	@BeforeEach
	void setUp() {
		String apiKey = getEnv("TESTOBJECT_API_KEY_ANDROID");
		initAndroidDriver(apiKey);
	}

	@Test
	public void test() throws InterruptedException {
		String endpoint = getEnv("DESTINATION_URL");
		driver.findElementById("endpoint").sendKeys(endpoint);
		driver.findElementById("button").click();
		Thread.sleep(3000);
		String responseStatus = driver.findElementById("response_status").getText();
		String content = driver.findElementById("content").getText();

		System.out.println("Response: " + responseStatus);
		System.out.println(content);

		assertEquals(200, Integer.parseInt(responseStatus));
	}
}
