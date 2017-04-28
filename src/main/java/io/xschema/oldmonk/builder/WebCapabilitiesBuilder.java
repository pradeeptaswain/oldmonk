package io.xschema.oldmonk.builder;

import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.xschema.oldmonk.exception.CapabilityNotPresentException;
import io.xschema.oldmonk.profile.BrowserProfile;
import io.xschema.oldmonk.profile.ChromeBrowserProfile;
import io.xschema.oldmonk.profile.FirefoxBrowserProfile;
import io.xschema.oldmonk.profile.IEBrowserProfile;

/**
 * Build capabilities for different browsers.
 */
public class WebCapabilitiesBuilder extends CapabilitiesBuilder
{
    private DesiredCapabilities capabilities;

    public WebCapabilitiesBuilder()
    {
        capabilities = new DesiredCapabilities();
    }

    @Override
    public WebCapabilitiesBuilder addBrowser(String browser)
    {
        capabilities.setBrowserName(browser);
        return this;
    }

    @Override
    public WebCapabilitiesBuilder addVersion(String version)
    {
        capabilities.setVersion(version);
        return this;
    }

    @Override
    public WebCapabilitiesBuilder addPlatform(final String platform)
    {
        Platform platformName = null;

        switch (platform.toLowerCase())
        {
            case "windows":
                platformName = Platform.WINDOWS;
                break;
            case "xp":
                platformName = Platform.XP;
                break;
            case "linux":
                platformName = Platform.LINUX;
                break;
            case "mac":
                platformName = Platform.MAC;
                break;
            default:
                platformName = Platform.WINDOWS;
                break;
        }

        capabilities.setPlatform(platformName);
        return this;
    }

    @Override
    public DesiredCapabilities build()
    {
        return capabilities;
    }

    @Override
    public WebCapabilitiesBuilder addBrowserProfile(BrowserProfile browserProfile) throws CapabilityNotPresentException
    {
        String browser = capabilities.getBrowserName();

        if (browser.equalsIgnoreCase("firefox"))
        {
            if (browserProfile instanceof FirefoxBrowserProfile)
            {
                capabilities.setCapability(FirefoxDriver.PROFILE, browserProfile.createProfile());
            }

        } else if (browser.equalsIgnoreCase("ie"))
        {
            if (browserProfile instanceof IEBrowserProfile)
            {
                @SuppressWarnings("unchecked")
                Map<String, Boolean> ieCapabilities = (Map<String, Boolean>) browserProfile.createProfile();
                Iterator<Map.Entry<String, Boolean>> it = ieCapabilities.entrySet().iterator();

                while (it.hasNext())
                {
                    Map.Entry<String, Boolean> keyValuePair = it.next();
                    capabilities.setCapability(keyValuePair.getKey(), keyValuePair.getValue());
                }
            }
        } else if (browser.equalsIgnoreCase("chrome"))
        {
            if (browserProfile instanceof ChromeBrowserProfile)
            {
                capabilities.setCapability(ChromeOptions.CAPABILITY, browserProfile.createProfile());
            }
        } else
        {
            throw new CapabilityNotPresentException("Unable to create capability for adding browser profile!");
        }

        return this;
    }

    @Override
    public CapabilitiesBuilder addProxy(String host, int port)
    {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("host" + ":" + port);

        capabilities.setCapability("proxy", proxy);

        return this;
    }

    @Override
    public CapabilitiesBuilder addBrowserDriverExecutablePath(String path)
    {
        if (null != path)
        {
            if (capabilities.getBrowserName().equalsIgnoreCase("chrome"))
            {
                System.setProperty("webdriver.chrome.driver", path);
            } else if (capabilities.getBrowserName().equalsIgnoreCase("firefox"))
            {
                System.setProperty("webdriver.gecko.driver", path);
            }
        }
        
        return this;
    }
}
