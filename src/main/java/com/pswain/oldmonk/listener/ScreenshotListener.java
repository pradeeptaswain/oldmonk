package com.pswain.oldmonk.listener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

/**
 * Class responsible for taking screenshots in case of test failures. This
 * listener class must be included in testng.xml for taking failure screenshots.
 */
public class ScreenshotListener extends TestListenerAdapter
{
    private static final Logger LOGGER     = LoggerFactory.getLogger(ScreenshotListener.class.getName());

    private static String       currentDir = null;

    static
    {
        try
        {
            currentDir = new File(".").getCanonicalPath();
        } catch (IOException realCause)
        {
            LOGGER.debug("Error while detecting current working dir. Reason:" + realCause.getMessage());
        }
    }

    /**
     * Take screenshot in case of test failure.
     */
    @Override
    public void onTestFailure(ITestResult tr)
    {
        super.onTestFailure(tr);

        WebDriver webDriver = findWebDriverByReflection(tr);

        if (webDriver == null)
        {
            LOGGER.debug(String
                    .format("The test class '%s' does not have any field of type 'org.openqa.selenium.WebDriver'. "
                            + "ScreenshotTestListener can not continue.", tr.getTestClass().getName()));
            return;
        }

        File f = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

        Format formatter = new SimpleDateFormat("dd_MMM_hh_mm_ss");

        String timestamp = formatter.format(new Date());
        String fileName = tr.getTestClass().getRealClass().getSimpleName() + "_" + timestamp + ".png";

        Path screenshotPath = null;

        try
        {
            screenshotPath = Paths.get(currentDir, "target", fileName);

            LOGGER.info("copying failure screenshot file '" + fileName + "' to '" + screenshotPath + "'");

            Files.copy(f.toPath(), screenshotPath, StandardCopyOption.REPLACE_EXISTING);
            Reporter.log("[Console Log] Screenshot saved in " + screenshotPath);
        } catch (IOException e)
        {
            LOGGER.debug("Error during the screenshot copy file operation:" + e.getMessage());
        }
    }

    /**
     * Find WebDriver instance by querying the running test class.
     */
    private WebDriver findWebDriverByReflection(ITestResult tr)
    {
        Class<?> c = tr.getInstance().getClass();
        Field[] fields = c.getSuperclass().getDeclaredFields();
        WebDriver webDriver = null;

        for (Field field : fields)
        {
            if (field.getType() == WebDriver.class)
            {
                try
                {
                    field.setAccessible(true);
                    webDriver = (WebDriver) field.get(tr.getInstance());
                } catch (IllegalAccessException e)
                {
                    LOGGER.debug(String.format("error accessing [%s] property of the [%s] instance", field.getName(),
                            tr.getClass().getName()));
                    return null;
                }

                break;
            }
        }

        return webDriver;
    }
}
