package com.pswain.oldmonk.uitests;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.pswain.oldmonk.builder.WebCapabilitiesBuilder;
import com.pswain.oldmonk.factory.WebDriverFactory;
import com.pswain.oldmonk.utils.ObjectRepository;
import com.pswain.oldmonk.utils.ProjectConfigurator;

public class BaseTest
{
    protected static Properties config;
    protected WebDriver         driver;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) throws Exception
    {
        config = ProjectConfigurator.initializeProjectConfigurationsFromFile("project.properties");
        ObjectRepository.setRepositoryDirectory(config.getProperty("object.repository.dir"));
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext context) throws Exception
    {
        final String browser = context.getCurrentXmlTest().getParameter("browser");
        final String version = context.getCurrentXmlTest().getParameter("version");
        final String platform = context.getCurrentXmlTest().getParameter("platform");

        DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser)
                .addBrowserDriverExecutablePath(config.getProperty("chrome.driver.path")).addVersion(version)
                .addPlatform(platform).build();
        
        driver = new WebDriverFactory().createDriver(caps);

        driver.get(config.getProperty("url"));
    }

    @AfterClass(alwaysRun = true)
    public void afterClass(ITestContext context) throws Exception
    {
        if (null != driver)
        {
            driver.close();
            driver.quit();
        }
    }
}
