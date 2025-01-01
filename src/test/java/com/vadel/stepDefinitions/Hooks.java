package com.vadel.stepDefinitions;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.vadel.base.TestBase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Hooks {

    WebDriver driver = TestBase.getDriver();

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot");
            try {
                File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(source, new File("screenshots/" + scenario.getName() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        driver.quit();
    }
    @Before
    public void setUp() {
        driver = TestBase.getDriver();
    }

    @After
    public void tearDown() {
        TestBase.closeDriver(); // Ferme WebDriver après chaque scénario
    }

}
