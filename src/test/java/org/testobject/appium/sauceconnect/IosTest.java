package org.testobject.appium.sauceconnect;

import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IosTest extends AbstractTest {
	private IOSDriver driver;

	@BeforeEach
	void setUp() {
		String apiKey = getEnv("TESTOBJECT_API_KEY_IOS");
		DesiredCapabilities desiredCapabilities = createCapabilities(apiKey);
		driver = new IOSDriver(getAppiumServer(), desiredCapabilities);
	}

	@Test
	public void test() throws InterruptedException {
		String endpoint = getEnv("DESTINATION_URL");
		driver.findElementById("endpoint_text_field").sendKeys(endpoint);
		driver.findElementById("get_button").click();
		Thread.sleep(5000);
		String responseStatus = driver.findElementById("status_code_label").getText();
		String content = driver.findElementById("response_content_label").getText();

		System.out.println("Response: " + responseStatus);
		System.out.println(content);

		assertEquals(200, Integer.parseInt(responseStatus));
	}

	@AfterEach
	void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
