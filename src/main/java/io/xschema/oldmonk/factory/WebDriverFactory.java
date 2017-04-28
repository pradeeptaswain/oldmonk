package io.xschema.oldmonk.factory;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Create Selenium/WebDriver Factory.
 */
public class WebDriverFactory implements DriverFactory
{
    private WebDriver driver;

    /**
     * Create WebDriver instance based on the capabilities passed.
     */
    public WebDriver createDriver(DesiredCapabilities caps)
    {
        String browser = caps.getBrowserName();

        if (browser.equalsIgnoreCase("firefox"))
        {
            driver = new FirefoxDriver(caps);
        } else if (browser.equalsIgnoreCase("ie"))
        {
            driver = new InternetExplorerDriver(caps);
        } else if (browser.equalsIgnoreCase("chrome"))
        {
            driver = new ChromeDriver(caps);
        }

        return driver;
    }

    /**
     * Create RemoteWebDriver instance based on the capabilities passed.
     */
    public WebDriver createDriver(URL remoteHubUrl, DesiredCapabilities caps) throws MalformedURLException
    {
        driver = new RemoteWebDriver(remoteHubUrl, caps);
        return driver;
    }
}
