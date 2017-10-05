package org.testobject.appium.sauceconnect;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AndroidTest extends AbstractTest {
	private AndroidDriver driver;

	@BeforeEach
	void setUp() {
		String apiKey = getEnv("TESTOBJECT_API_KEY_ANDROID");
		DesiredCapabilities desiredCapabilities = createCapabilities(apiKey);
		System.out.println("Initializing Android test with capabilities: " + desiredCapabilities);
		driver = new AndroidDriver(getAppiumServer(), desiredCapabilities);
		System.out.println("Returned capabilities: " + driver.getCapabilities());
	}

	@Test
	public void test() throws InterruptedException {
		String endpoint = getEnv("DESTINATION_URL");
		driver.findElement(By.id("com.testobject.httprequest:id/endpoint")).sendKeys(endpoint);
		driver.findElementById("button").click();
		Thread.sleep(3000);
		String responseStatus = driver.findElementById("response_status").getText();
		String content = driver.findElementById("content").getText();

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
