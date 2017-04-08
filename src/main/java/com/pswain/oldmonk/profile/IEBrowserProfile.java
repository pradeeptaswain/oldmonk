package com.pswain.oldmonk.profile;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Create browser profile for internet explorer. Usually this is represented as
 * Desired Capability in WebDriver where we can set different options for IE.
 */
public class IEBrowserProfile extends BrowserProfile
{
    protected Map<String, Boolean> ieCapabilitiesMap;

    public IEBrowserProfile()
    {
        ieCapabilitiesMap = new LinkedHashMap<String, Boolean>();
    }

    @Override
    public Object createProfile()
    {
        return ieCapabilitiesMap;
    }

    public IEBrowserProfile enablePersistentHovering(boolean option)
    {
        ieCapabilitiesMap.put(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, option);
        return this;
    }

    public IEBrowserProfile setRequireWindowFocus(boolean option)
    {
        ieCapabilitiesMap.put(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, option);
        return this;
    }

    public IEBrowserProfile introduceFlakinessByIgnoringSecurityDomains(boolean option)
    {
        ieCapabilitiesMap.put(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, option);
        return this;
    }
}
