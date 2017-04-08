# OldMonk
OldMonk is an Automation framework for developing tests using Selenium/WebDriver and TestNG

This framework abstracts away the boiler plate code that are needed for setting up any Selenium/WebDriver/TestNG based automation framework. It provides the below abilities:

- Logging
- Generation of screenshots in case of test failure
- Run tests on different browsers/platforms
- Run tests locally, on in-house selenium grid, on sauce labs or on browserstack
- Generate customized reports
- Abstract away driver creation, capability creation based on browser
- Rerun failed test cases
- Inject test data from data source like xml
- Store/retrieve locators from object repositories
- Scalable and Maintainable
- Configurable project parameters like, application url, time waits, selenium grid information etc.

To use the framework, clone the repo and run the command:
```
mvn clean package
```
It will generate `oldmonk-jar-with-dependencies.jar` file. Now create a simple maven project using the command 
```
mvn archetype:generate -DgroupId={project-packaging}
   -DartifactId={project-name}
   -DarchetypeArtifactId=maven-archetype-quickstart
   -DinteractiveMode=false
```

Import this project in eclipse and any other Java IDE. Add the `oldmonk-jar-with-dependencies.jar` file to build path.

### Steps to create a new test script

Create a new file `BaseTest.java` in `src/main/java` folder. We'll be using this as a base test class, that all test script classes will extend. This class will contain few configuration methods, that will initialize the project and create appropriate driver. At the end of the test, it will close the driver.

```
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

```
### Create a project configuration file

Create a new file `project.properties` in the root of the project and add following  lines:

```
# Base application url
url=http://www.yahoo.com

# Directory where element locators will be stored. All element locator files should end with .properties.
# Locators can be specified in this format: locator_name=locator_strategy,locator
# For ex: mail_link=XPATH,//span[text()='Mail']. Supported locator strategies are 'XPATH', 'ID', 'NAME', 
# 'CLASS_NAME', 'TAG_NAME', 'CSS_SELECTOR', 'LINK_TEXT', 'PARTIAL_LINK_TEXT'
object.repository.dir=src/main/resources

# Application modules
application.modules=login,booking,search

# Test Groups
test.groups=smoke,regression

# Number of times to rerun the test in case of test failure. '0' means, failed tests will not be
# executed again
max.retry.count=0

# Path to chromedriver executable file
chrome.driver.path=/Users/pradeepta/Tools/chromedriver
```

### Create Test Script

Create a test script to open browser, navigate to yahoo.com and click on 'mail' link

```
package com.pswain.oldmonk.uitests;

import org.testng.annotations.Test;

import com.pswain.oldmonk.utils.TestDataProvider;

public class TestClass extends BaseTest
{
    @Test(groups = { "smoke" }, dataProvider = "testdataprovider", dataProviderClass = TestDataProvider.class)
    public void testMethod(String email, String password) throws Exception
    {
        YahooTestPage tPage = new YahooTestPage(driver);
        tPage.clickOnMailLink();
        tPage.typeEmailAddress(email);
        tPage.typePassword(password);
        HomePage hPage = tPage.clickOnSigninButton();
        
        // Do something with home page
    }
}

```
Note that, email and password parameters are injected into the test method from outside data source. To create the data source, create a new file in `src/main/resources` directory. For example our testdata.xml file looks like this:

```
<?xml version="1.0" encoding="UTF-8"?>

<testdatasuite>
	<testclass name="com.pswain.oldmonk.uitests.TestClass">
		<dataset>
			<email>test@yahoo.com</email>
			<password>mypassword</password>
		</dataset>
	</testclass>
</testdatasuite>
```
You can have any number of `dataset` sections inside `testclass` section. If there are multiple `dataset` sections, test method will be executed multiple times with different `dataset` test data.

## Run Test Script

To run the test script, we need a testng xml file. Create an xml file in the root of the directory and add the following content:

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Sample Test Suite" verbose="1" parallel="classes" thread-count="1">
	<listeners>
		<listener class-name="com.pswain.oldmonk.listener.ScreenshotListener" />
		<listener class-name="com.pswain.oldmonk.listener.RetryListener" />
	</listeners>

	<parameter name="browser" value="chrome" />
	<parameter name="version" value="56.0" />
	<parameter name="platform" value="windows 8" />
	<parameter name="testdataxml" value="src/main/resources/testdata.xml" />

	<test name="Sample Smoke Tests">
		<groups>
			<run>
				<include name="smoke" />
			</run>
		</groups>

		<packages>
			<package name="com.pswain.oldmonk.uitests" />
		</packages>

	</test>
</suite>
```
At this point, browser will be launched and test will be executed. Reports will be generated inside test-output folder.

# Additional Information

## Creating browser profile

We can set different profile parameters for different browsers using browser profiles. Currently supported browser profiles are ChromeBrowserProfile, FirefoxBrowserProfile and IEBrowserProfile. To use a profile, use it like this:

```
FirefoxBrowserProfile ffProfile = new FirefoxBrowserProfile();
	
ffProfile.setAcceptUntrustedCertificates(true).showDownloadManagerWhenStarting(false)
                .setDownloadDirectory("/Users/pradeepta/Downloads");
        
DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser)
                .addBrowserDriverExecutablePath(config.getProperty("gecko.driver.path")).addVersion(version)
                .addPlatform(platform).addBrowserProfile(ffProfile).build();

driver = new WebDriverFactory().createDriver(caps);

```
## Using proxy

To access the websites using proxy, use it like:

```
DesiredCapabilities caps = new WebCapabilitiesBuilder().addBrowser(browser).addProxy("192.168.3.60", 5678).
                .addBrowserDriverExecutablePath(config.getProperty("gecko.driver.path")).addVersion(version)
                .addPlatform(platform).addBrowserProfile(ffProfile).build();
```

## Running tests using selenium GRID

Assuming that selenium Grid is configued on localhost and is running on port 4444, you can run your tests on grid using `GridUrlBuilder`

```
URL remoteHubUrl = new SeleniumGridUrlBuilder().addProtocol(Protocol.HTTP).addSeleniumHubHost("127.0.0.1")
                .addSeleniumHubPort(4444).build();

driver = new WebDriverFactory().createDriver(remoteHubUrl, caps);
```

## Supported element actions

To check the supported element actions/supported selenium commands see [BasePage.java](src/main/java/com/pswain/oldmonk/core/BasePage.java) 
