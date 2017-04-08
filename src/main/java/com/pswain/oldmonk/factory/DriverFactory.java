package com.pswain.oldmonk.factory;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Create WebDriver instance based on the capabilities passed.
 */
public interface DriverFactory
{
    /**
     * Create driver based on only capabilities passed.
     * 
     * @param caps
     *            - desired browser capabilities
     * @return - WebDriver instance
     */
    WebDriver createDriver(DesiredCapabilities caps);

    /**
     * Create RemoteWebDriver instance based on the capabilities passed.
     * 
     * @param remoteHubUrl
     *            - Selenium GRID url
     * @param caps
     *            - desired browser capabilities
     * @return - RemoteWebDriver instance
     * @throws MalformedURLException
     *             - throw this exception, if selenium hub url is not well
     *             formed
     */
    WebDriver createDriver(URL remoteHubUrl, DesiredCapabilities caps) throws MalformedURLException;
}
